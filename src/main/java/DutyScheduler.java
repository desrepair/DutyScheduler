
import static spark.Spark.*;
import SchedulingHeuristic.*;

public class DutyScheduler {


    public static void main(String[] args) {
         
        get("/", (request, response) -> {
            //Serve the main page.
            return "This is the main page.";
        });
        
        post("/submitCal", (request, response) -> {
            //Create Duty Calendar, associate ID.
            //Returns a scheduled calendar.
            return "You have submitted a calendar.";
        });
        
        get("/authorize", (request, response) -> {
            //Check for authorization.
            com.google.api.client.auth.oauth2.Credential credential
                    = GoogleCalendarApiAccess.getStoredCredential("ID"); //TODO: ID system.
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
            }
            //Extracts the code.
            String[] parts = authCode.split("=");
            if (parts[0].equals("code")) {
                //Requests token.
                com.google.api.client.auth.oauth2.Credential credential
                        = GoogleCalendarApiAccess.getTokenCredential(
                        parts[1],
                        "ID");
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
        
        post("/pushToCal", (request, repsonse) -> {
            //Check for authentication.
            com.google.api.client.auth.oauth2.Credential credential
                    = GoogleCalendarApiAccess.getStoredCredential("ID");
            if (credential == null) {
                halt(401, "Unauthorized");
            }
            //Create calendar based on JSON
            //Push calendar to Google.
            return "Your calendar has been exported to your Google Calendar";
        });

        System.out.println("Server online.");
    }

}
