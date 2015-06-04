
import java.util.UUID;

import static spark.Spark.*;

import SchedulingHeuristic.*;

public class DutyScheduler {


    public static void main(String[] args) {
         
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
            //Create Duty Calendar
            //Associate cookie.
            String uuid = request.cookie("DutyScheduler");
            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
                response.cookie("DutyScheduler", uuid);
            }
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
            com.google.api.client.auth.oauth2.Credential credential
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
                com.google.api.client.auth.oauth2.Credential credential
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
                halt(401, "Missing required cookie");
            }
            //Check for authentication.
            com.google.api.client.auth.oauth2.Credential credential
                    = GoogleCalendarApiAccess.getStoredCredential(uuid);
            if (credential == null) {
                halt(401, "Unauthorized");
            }
            //Create calendar based on JSON.
            //Push calendar to Google.
            return "Your calendar has been exported to your Google Calendar";
        });

        System.out.println("Server online.");
    }

}
