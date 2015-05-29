/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SchedulingHeuristic;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;

/**
 *
 * @author desrepair
 */
public class GoogleCalendarApiAccess {
        /** Application name. */
    private static final String APPLICATION_NAME =
        "Duty Scheduler";

    /** Directory to store user credentials. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        System.getProperty("user.home"), ".credentials/duty-calendar");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart. */
    private static final List<String> SCOPES =
        Arrays.asList(CalendarScopes.CALENDAR);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    private static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
            GoogleCalendarApiAccess.class.getResourceAsStream("../client_secret.json");
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     * @return an authorized Calendar client service
     * @throws IOException
     */
    private static com.google.api.services.calendar.Calendar
        getCalendarService() throws IOException {
        Credential credential = authorize();
        return new com.google.api.services.calendar.Calendar.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
        
    /**
     * Creates and returns a new Calendar.
     * @param service Google calendar API authorized calendar client service.
     * @param calDes Name of the calendar to be created.
     * @return Calendar model object created.
     * @throws IOException
     */
    private static com.google.api.services.calendar.model.Calendar
            createNewCalendar(com.google.api.services.calendar.Calendar service,
                    String calDes) throws IOException {
        com.google.api.services.calendar.model.Calendar cal = new Calendar();
        cal.setSummary(calDes);
        cal.setTimeZone("America/New_York");
        
        com.google.api.services.calendar.model.Calendar createdCalendar = service.calendars().insert(cal).execute();
        System.out.println(createdCalendar.getId());
        
        return createdCalendar;
    }
          
    /**
     * Retrieves a user's calendar from their calendarId.
     * @param service Google calendar API authorized calendar client service.
     * @param calId Id of the calendar to retrieve.
     * @return Calendar model object retrieved, null if failed.
     */
    private static com.google.api.services.calendar.model.Calendar 
            getCalendar(
                    com.google.api.services.calendar.Calendar service,
                    String calId
          ) {
        try {
            com.google.api.services.calendar.model.Calendar target
                = service.calendars().get(calId).execute();
            System.out.println("Retrieved " + target.getSummary());
            return target;
        } catch (IOException e) {
            System.out.println("Calendar retrieval failed.");
            System.out.println(e);
            return null;
        }
    }
            
    /**
     * Creates an event on the given calendar id.
     * @param eventSummary Name of the event.
     * @param startDate Start date of the event.
     * @param endDate End date of the event.
     * @param service Google calendar API authorized calendar client service.
     * @param calId Id of the calendar to create the event on.
     * @throws IOException 
     */
    private static void createEvent(String eventSummary,
                    Date startDate,
                    Date endDate,
                    com.google.api.services.calendar.Calendar service,
                    String calId) throws IOException {
        com.google.api.services.calendar.model.Event target = new Event();
        target.setSummary(eventSummary);
        target.setLocation("");

        target.setAttendees(new ArrayList<>());
        com.google.api.client.util.DateTime start = new DateTime(true, startDate.getTime(), TimeZone.getTimeZone("America/New_York").getRawOffset());
        com.google.api.client.util.DateTime end = new DateTime(true, endDate.getTime(), TimeZone.getTimeZone("America/New_York").getRawOffset()); 
        target.setStart(new EventDateTime().setDate(start));
        target.setEnd(new EventDateTime().setDate(end));
        com.google.api.services.calendar.model.Event createdEvent
                = service.events().insert(calId, target).execute();
        System.out.println("Event created: " + createdEvent.getHtmlLink());
    }
    
    /**
     * Populates a given calendar with events given a duty calendar.
     * @param calId Calendar to populate
     * @param dutyCal List of duty blocks to populate the calendar with.
     */
    private static void populateCalendar(
            com.google.api.services.calendar.Calendar service,
            String calId,
            ArrayList<DutyBlock> dutyCal) throws IOException {
        for (DutyBlock block : dutyCal) {
            LocalDate start = block.getStartDate();
            Date startDate = new Date(start.getYear() - 1900,
                    start.getMonthValue() - 1,
                    start.getDayOfMonth());
            
            LocalDate end = block.getEndDate();
            Date endDate = new Date(end.getYear() - 1900,
                    end.getMonthValue() - 1,
                    end.getDayOfMonth());
            
            Iterator<Ra> iterator = block.getRasOnDuty().iterator();
            StringBuilder eventName = new StringBuilder();
            if (!iterator.hasNext()) {
                eventName.append("No RAs Assigned");
            } else {
                eventName.append(iterator.next().toString());
            }
            while (iterator.hasNext()) {
                eventName.append(" & ");
                eventName.append(iterator.next().toString());
            }
            createEvent(eventName.toString(), startDate, endDate, service, calId);
        }
    }
    
    /**
     * Creates a new Calendar and populates it with duty events.
     * @param calName Name of the calendar to create.
     * @param dutyCal List of DutyBlock objects to populate the calendar with.
     * @return 0 if successful, -2 if an error occurs.
     */
    public static int createNewDutyCalendar(String calName, ArrayList<DutyBlock> dutyCal) {
        try {
            com.google.api.services.calendar.Calendar service = getCalendarService();
            com.google.api.services.calendar.model.Calendar createdCal = createNewCalendar(service, calName);
            populateCalendar(service, createdCal.getId(), dutyCal);
            return 0;
        } catch (IOException e) {
            System.out.println("An error occured while connecting to Google");
            System.out.println(e);
            return -2;
        }
    }
    
    /**
     * Retrieves an existing Calendar and populates it with duty events.
     * @param calId Id of the calendar to populate.
     * @param dutyCal List of DutyBlock objects to populate the calendar with.
     * @return 0 if successful, -1 if retrieval failed, -2 if population failed.
     */
    public static int populateExistingDutyCalendar(String calId, ArrayList<DutyBlock> dutyCal) {
        try {
            com.google.api.services.calendar.Calendar service = getCalendarService();
            com.google.api.services.calendar.model.Calendar retrievedCal = getCalendar(service, calId);
            if (retrievedCal != null) {
                populateCalendar(service, retrievedCal.getId(), dutyCal);
                return 0;
            } else {
                return -1;
            }
        } catch (IOException e) {
            System.out.println("An error occured while connecting to Google");
            System.out.println(e);
            return -2;
        }
    }
    
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            //Creates an authorized google Calendar service.
            com.google.api.services.calendar.Calendar service =
                    getCalendarService();
            
            //Asks for the calendar ID.
            System.out.println("Please enter your Calendar ID (blank for new Calendar)");
            String calId = input.nextLine();
            
            //Gets the calendar
            com.google.api.services.calendar.model.Calendar targetCal;
            if (calId.isEmpty()) {
                System.out.println("Please enter your new Calendar's name.");
                String name = input.nextLine();
                targetCal = createNewCalendar(service, name);
            } else {
                targetCal = getCalendar(service, calId);
            }
            
            //
            if (targetCal != null) {
                createEvent("IT WORKS", new Date(115, 04, 28), new Date(115, 04, 28),
                        service, targetCal.getId());
            } else {
                System.out.println("No calendar by that name.");
            }
            
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
