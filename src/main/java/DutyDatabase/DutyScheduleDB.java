package DutyDatabase;



import SchedulingHeuristic.DutyBlock;
import SchedulingHeuristic.DutyBlockCodecProvider;
import SchedulingHeuristic.DutyCalendar;
import SchedulingHeuristic.LocalDateCodec;
import SchedulingHeuristic.Ra;
import SchedulingHeuristic.RaCodecProvider;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import java.time.LocalDate;


import java.util.ArrayList;
import java.util.HashMap;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * MonboDB Database for storing information pertaining to duty schedules.
 * Collections: DutySchedulerDB - stores ScheduledCalendar objects.
 * @author desrepair
 */
public class DutyScheduleDB {
    private MongoClient mongoClient;
    private MongoDatabase db;
    
    /**
     * Creates a connection to a running MongoDB instance using the required codecs.
     */
    public DutyScheduleDB() {
        //Create codec registry with LocalDateCodec.
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                CodecRegistries.fromCodecs(new LocalDateCodec()),
                CodecRegistries.fromProviders(
                        new RaCodecProvider(),
                        new DutyBlockCodecProvider(),
                        new ScheduledDutyCodecProvider()),
                MongoClient.getDefaultCodecRegistry());  
        MongoClientOptions options = MongoClientOptions.builder()
                .codecRegistry(codecRegistry).build();
        mongoClient = new MongoClient(new ServerAddress(), options);
        db = mongoClient.getDatabase("DutySchedulerDB");
    }
    
    /**
     * Stores a scheduled calendar in the database.
     * @param id ID of the user (unique to the user).
     * @param calendarName Name of the calendar to be stored (unique to calendars owned by the user).
     * @param cal List DutyBlocks comprising the calendar to be stored.
     */
    public void storeScheduledCalendar(String id,
            String calendarName,
            ArrayList<DutyBlock> cal) {
        //Access collection of scheduled calendars.
        MongoCollection<ScheduledDuty> collection = db.getCollection("ScheduledCalendars", ScheduledDuty.class);
        //Query parameter is uuid + calendarName.
        ScheduledDuty toInsert = new ScheduledDuty(id + calendarName, cal);
        //Insert doc to collection.
        collection.insertOne(toInsert);
    }
    
    /**
     * Retrieved a previously stored scheduled calendar in the database.
     * @param id ID of the user.
     * @param calendarName Name of the calendar to retrieve.
     * @return List of DutyBlocks comprising the calendar retrieved.
     */
    public ArrayList<DutyBlock> getScheduledCalendar(String id, String calendarName) {
        MongoCollection col = db.getCollection("ScheduledCalendars");
        Document testDoc = (Document) col.find(
                new Document("name", id + calendarName))
                .first();
        //Access collection of scheduled calendars.
        MongoCollection<ScheduledDuty> collection = db.getCollection("ScheduledCalendars").withDocumentClass(ScheduledDuty.class);
        //Search for specified calendar.
        ScheduledDuty doc = (ScheduledDuty) collection.find(
                new Document("name", id + calendarName))
                .first();
        return doc.getDutyCalendar();
    }
    
    /**
     * Removes a previously stored calendar from the database.
     * @param id ID of the user.
     * @param calendarName Name of the calendar to be removed.
     */
    public void removeScheduledCalendar(String id, String calendarName) {
        //Access collection of scheduled calendars.
        MongoCollection collection = db.getCollection("ScheduledCalendars");
        //Remove specified calendar.
        collection.deleteOne(new Document("name", id + calendarName));
    }
    
    public static void main(String[] args) {
        DutyCalendar scheduler = new DutyCalendar(
                LocalDate.of(2015, 05, 19), LocalDate.of(2015, 05, 26), 1, 1);
        HashMap<LocalDate, Double> points = new HashMap<>();
        points.put(LocalDate.of(2015, 05, 19), 1.0);
        points.put(LocalDate.of(2015, 05, 20), 1.0);
        points.put(LocalDate.of(2015, 05, 21), 1.5);
        points.put(LocalDate.of(2015, 05, 22), 2.0);
        points.put(LocalDate.of(2015, 05, 23), 2.0);
        points.put(LocalDate.of(2015, 05, 24), 1.0);
        points.put(LocalDate.of(2015, 05, 25), 1.0);
        points.put(LocalDate.of(2015, 05, 26), 1.0);
        scheduler.setDayValues(points);
        ArrayList<LocalDate> micksBlackout = new ArrayList<>();
        micksBlackout.add(LocalDate.of(2015, 05, 20));
        micksBlackout.add(LocalDate.of(2015, 05, 21));
        scheduler.addRa("Mick", micksBlackout);
        scheduler.addRa("Tatev", new ArrayList<>());
        scheduler.addRa("David", new ArrayList<>());
        scheduler.addRa("Cliff", new ArrayList<>());
        ArrayList<DutyBlock> result = scheduler.assignDuty();
        
        System.out.println("Storing in DB");
        DutyScheduleDB db = new DutyScheduleDB();
        db.storeScheduledCalendar("id", "name", result);
        
        System.out.println("Retrieving");
        ArrayList<DutyBlock> retrieved = db.getScheduledCalendar("id", "name");
        for (DutyBlock blk : retrieved) {
            System.out.println(blk.getStartDate() + ":");
            for (Ra ra : blk.getRasOnDuty()) {
                System.out.println("\t" + ra);
            }
            System.out.println();
        }
        
        db.removeScheduledCalendar("id", "name");
    }
    
}
