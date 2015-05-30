
import static spark.Spark.*;

public class DutyScheduler {


    public static void main(String[] args) {
//        
//
//        // List the next 10 events from the primary calendar.
//        DateTime now = new DateTime(System.currentTimeMillis());
//        Events events = service.events().list("primary")
//            .setMaxResults(10)
//            .setTimeMin(now)
//            .setOrderBy("startTime")
//            .setSingleEvents(true)
//            .execute();
//        List<Event> items = events.getItems();
//        if (items.size() == 0) {
//            System.out.println("No upcoming events found.");
//        } else {
//            System.out.println("Upcoming events");
//            for (Event event : items) {
//                DateTime start = event.getStart().getDateTime();
//                if (start == null) {
//                    start = event.getStart().getDate();
//                }
//                System.out.printf("%s (%s)\n", event.getSummary(), start);
//            }
//        }

        // spark test
        get("/hello", (req, res) -> "Hello Universe");

        System.out.println("hello!!");
    }

}
