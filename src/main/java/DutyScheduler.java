/*
 * Duty Scheduler: A scheduler for point based duty.
 * Copyright (c) 2015 Khetthai Laksanakorn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * For source code and contact information for Khetthai Laksanakorn,
 * see <http://www.github.com/desrepair/DutyScheduler>.
 *
 */

import DutyDatabase.DutyScheduleDB;
import java.util.UUID;

import static spark.Spark.*;

import com.google.api.client.auth.oauth2.Credential;

import SchedulingHeuristic.*;

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
        
        get("/submitCal", (request, response) -> {
            //Associate cookie.
            String uuid = request.cookie("DutyScheduler");
            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
                response.cookie("DutyScheduler", uuid);
            }
            //Parse request and create Duty Calendar
            //Parse JSON.
            //DutyCalendar calendar = new DutyCalendar(LocalDate.MIN, LocalDate.MIN, 1, 1);
            //calendar.setDayValues(null);
            //calendar.addRa("Mick", new ArrayList<LocalDate>());
            //calendar.addRA("David", new ArrayList<LocalDate>());
            //ArrayList<DutyBlock> blocks = calendar.assignDuty();
            //Persist in database
            //database.storeScheduledCalendar(uuid, request.name(), blocks);
            //Returns a scheduled calendar.
            return "You have submitted a calendar.";
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
