package DutySchedulerApi;


import DutyDatabase.DutyScheduleDB;
import SchedulingHeuristic.DutyBlock;
import SchedulingHeuristic.DutyCalendar;
import com.google.api.client.auth.oauth2.Credential;
import java.util.UUID;
import static spark.Spark.*;
import com.google.gson.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DutyScheduler {

    public static void main(String[] args) {
        DutyScheduleDB database = new DutyScheduleDB();
         
        get("/", (request, response) -> {
            //Associate cookie.
            String uuid = request.cookie("DutyScheduler");
            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
                response.cookie("DutyScheduler", uuid);
            }
            //Serve the main page.
            return "This is the main page.";
        });
        
        post("/submitCal", (request, response) -> {
            //Associate cookie.
            String uuid = request.cookie("DutyScheduler");
            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
                response.cookie("DutyScheduler", uuid);
            }
            
            //Parse request and create Duty Calendar
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
            gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
            gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateInstanceCreator());
            Gson gson = gsonBuilder.create();
            CalendarCreationRequest content = gson.fromJson(request.body(),
                    CalendarCreationRequest.class);
            
            DutyCalendar calendar = new DutyCalendar(
                    content.getStartDate(),
                    content.getEndDate(),
                    content.getBlockSize(),
                    content.getRasPerBlock());
            
            //Set duty block points.
            HashMap<LocalDate, Double> dayValues = new HashMap<>();
            for (DayPoints day : content.getPointsPerDay()) {
                dayValues.put(day.getDate(), day.getPointValue());
            }
            calendar.setDayValues(dayValues);
            
            //Set RA blackout dates.
            for (RaBlackoutDates ra : content.getRas()) {
                calendar.addRa(
                        ra.getName(),
                        new ArrayList<>(Arrays.asList(ra.getBlackoutDates())));
            }
            
            ArrayList<DutyBlock> blocks = calendar.assignDuty();
            System.out.println("\n____________");
            System.out.println(calendar.toString());
            System.out.println(calendar.printRaPointValues());
            database.storeScheduledCalendar(uuid, content.getCalendarName(), blocks);
            return gson.toJson(blocks);
        });
        
        get("/authorize", (request, response) -> {
            //Check for cookie.
            String uuid = request.cookie("DutyScheduler");
            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
                response.cookie("DutyScheduler", uuid);
            }
            //Check for authorization.
            Credential credential
                    = GoogleCalendarApiAccess.getStoredCredential(uuid); //TODO: ID system.
            //If not authorized, redirect to authorization.
            if (credential == null) {
                String url = GoogleCalendarApiAccess.requestAuthorizationCode();
                response.status(302);
                response.body("You are being redirected.");
                response.redirect(url);
            } 
            //If authorized, activate "Export Calendar" button
            return "You are already authorized";
        });
        
        get("/oauth2callback", (request, response) -> {
            //Checks for code
            String authCode = request.queryString();
            if (authCode == null) {
                halt(403, "Forbidden");
                return "";
            }
            
            //Checks for cookie.
            String uuid = request.cookie("DutyScheduler");
            if (uuid == null) {
                halt(403, "Missing required cookie.");
            }
            //Extracts the code.
            String[] parts = authCode.split("=");
            if (parts[0].equals("code")) {
                //Requests token.
                Credential credential
                        = GoogleCalendarApiAccess.getTokenCredential(
                        parts[1],
                        uuid); //TODO: Get ID here.
                //Refreshes client page, asks for calendar submission.
                //Maybe activate a grayed out "Export Calendar" button.
                response.redirect("khetthail.me:4567/");
                return "Authorized.";
            } else if (parts[0].equals("error")) {
                return "You did not authenticate";
            }
            halt(403, "Forbidden");
            return "";
        });
        
        get("/pushToCal", (request, repsonse) -> {
            //Check for cookie.
            String uuid = request.cookie("DutyScheduler");
            if (uuid == null) {
                halt(401, "Missing required cookie. Please visit main page.");
            }
            //Check for authentication.
            Credential credential
                    = GoogleCalendarApiAccess.getStoredCredential(uuid);
            if (credential == null) {
                halt(401, "Unauthorized");
            }
            //Retrieve scheduled calendar.
            //ArrayList<DutyBlock> blocks = database.getScheduledCalendar(uuid, request.name());
            //Push calendar to Google.
            //GoogleCalendarApiAccess.createNewDutyCalendar(calendar.getName(), uuid, calendar.getCalendar());
            return "Your calendar has been exported to your Google Calendar";
        });

        System.out.println("Server online.");
    }
}
