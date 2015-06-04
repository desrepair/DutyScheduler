package DutyDatabase;


import SchedulingHeuristic.DutyBlock;
import java.util.ArrayList;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;



/**
 * Placeholder object stored within the DutySchedulerDB collection.
 * @author desrepair
 */
public class ScheduledDuty implements Bson {
    private ObjectId _id;
    String name;
    private ArrayList<DutyBlock> dutyCalendar;
    
    /**
     * Initializes an instance of a ScheduledDuty object.
     * @param id ID of the new object.
     * @param n Name of the calendar (userId + calendarName).
     * @param cal List of DutyBlock objects representing a scheduled calendar.
     */
    public ScheduledDuty(ObjectId id, String n, ArrayList cal) {
        _id = id;
        name = n;
        dutyCalendar = cal;
    }
    
    /**
     * Initializes an instance of a ScheduledDuty object without an Id (set to null).
     * @param n Name of the calendar (userId + calendarName).
     * @param cal List of DutyBlock objects representing a scheduled calendar.
     */
    public ScheduledDuty(String n, ArrayList cal) {
        name = n;
        dutyCalendar = cal;
    }
    
    /**
     * Retrieves the ID of the ScheduledDuty object.
     * @return ID of the ScheduledDuty object.
     */
    public ObjectId getId() {
        return _id;
    }
    
    /**
     * Retrieves the name of the ScheduledDuty object.
     * @return Name of the ScheduledDuty object.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Retrieves the scheduled calendar stored in the object.
     * @return List of DutyBlock objects representing a scheduled calendar.
     */
    public ArrayList<DutyBlock> getDutyCalendar() {
        return dutyCalendar;
    }
    
    /**
     * Sets the ID of the ScheduledCalendar object.
     * @param id ID to assign to the object.
     */
    public void setId(ObjectId id) {
        _id = id;
    }
    
    /**
     * Sets the name of the ScheduledCalendar object.
     * @param n Name to assign to the object.
     */
    public void setName(String n) {
        name = n;
    }
    
    /**
     * Sets the scheduled duty calendar within this object.
     * @param duty List of DutyBlocks to assign to the object.
     */
    public void setDutyCalendar(ArrayList<DutyBlock> duty) {
        dutyCalendar = duty;
    }
    
    /**
     * Assigns a new ID to this object.
     * @return This ScheduledDuty object, with a new ID assigned.
     */
    public ScheduledDuty withNewObjectId() {
        setId(new ObjectId());
        return this;
    }

    /**
     * Converts this object into a BsonDocument
     * @param <TDocument>
     * @param type
     * @param cr
     * @return 
     */
    @Override
    public <TDocument> BsonDocument toBsonDocument(Class<TDocument> type, CodecRegistry cr) {
        if (type == ScheduledDuty.class) {
            return new BsonDocumentWrapper<ScheduledDuty> (this, cr.get(ScheduledDuty.class));
        }
        return null;
        
    }
}
