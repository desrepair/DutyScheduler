package SchedulingHeuristic;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.bson.Document;

/**
 * Represents an RA.
 * @author desrepair
 */
public class Ra implements Comparable<Ra> {
    private final String name;
    private double pointsTaken;
    private LocalDate previousDutyDay;
    private ArrayList<LocalDate> blackoutDates;
    private ArrayList<LocalDate> dutyDays;

    /**
     * Creates an RA object.
     * @param n The name of the RA.
     * @param b The list of blackout dates for the RA.
     */
    public Ra(String n, ArrayList<LocalDate> b) {
        name = n;
        pointsTaken = 0;
        blackoutDates = b;
        previousDutyDay = DutyCalendar.currentBlock.getStartDate();
        dutyDays = new ArrayList<>();
    }
    
    public Ra(String n, Double points, LocalDate prev, ArrayList<LocalDate> blackout, ArrayList<LocalDate> duty) {
        name = n;
        pointsTaken = points;
        previousDutyDay = prev;
        blackoutDates = blackout;
        dutyDays = duty;
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * Returns the point value of duty assigned to the RA.
     * @return Point value of duty assigned to the RA in double form.
     */
    public double getPointsTaken() {
        return pointsTaken;
    }
    
    public LocalDate getPreviousDutyDay() {
        return previousDutyDay;
    }
    
    public ArrayList<LocalDate> getBlackoutDates() {
        return blackoutDates;
    }
    
    public ArrayList<LocalDate> getDutyDays() {
        return dutyDays;
    }
    
    /**
     * Returns the days since the RA was last on duty. Default 14.
     * @return Days since RA last on duty.
     */
    private int daysSinceLastDuty() {
        if (previousDutyDay == null) {
            return DutyCalendar.numberOfRAs;
        }
        return (int) previousDutyDay.until(DutyCalendar.currentBlock.getStartDate(),
                ChronoUnit.DAYS);
    }
    
    /**
     * Assigns the day for the RA to be on duty.
     * @param day A day the RA is on duty.
     * @param points Point value of the day of duty.
     */
    public void assignDay(LocalDate day, double points) {
        previousDutyDay = day;
        dutyDays.add(day);
        pointsTaken += points;
    }
    
    /**
     * Adds a date when the RA is unavailable for duty.
     * @param blackout Date when RA is unavailable.
     */
    public void addBlackoutDate(LocalDate blackout) {
        blackoutDates.add(blackout);
    }
    
    /**
     * Removes a date where the RA was previously unavailab.e
     * @param blackout Date when RA now available.
     */
    public void removeBlackoutDate(LocalDate blackout) {
        blackoutDates.remove(blackout);
    }
    
    /**
     * Calculates the RA's priority for a certain duty day.
     * Lower value indicates higher priority.
     * @param b Block to determine RA's priority for.
     * @return RA's priority for the duty day.
     */
    public double calculateDayWorth(DutyBlock b) {
        for (LocalDate blackoutDate : blackoutDates) {
            if (blackoutDate.isEqual(b.getStartDate())
                    || (blackoutDate.isAfter(b.getStartDate()) 
                        && blackoutDate.isBefore(b.getEndDate().plusDays(1)))) {
                return Double.MAX_VALUE;
            }
        }
        //Implementation is a minHeap - more days since last duty means more desirable.
        return pointsTaken + .01 * (DutyCalendar.numberOfRAs
                - daysSinceLastDuty());
    }

    @Override
    public int compareTo(Ra o) {
        return (int) (calculateDayWorth(DutyCalendar.currentBlock)
                - o.calculateDayWorth(DutyCalendar.currentBlock));
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    /**
     * Creates a BSON representation of the object.
     * @return A BSON representation of the object.
     */
    public Document toBson() {
        Document document = new Document();
        document.append("name", name)
                .append("pointsTaken", pointsTaken)
                .append("previousDutyDay", previousDutyDay)
                .append("blackoutDates", blackoutDates)
                .append("dutyDays", dutyDays);
        return document;
    }
}
