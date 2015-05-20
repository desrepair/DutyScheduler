package SchedulingHeuristic;

import java.time.LocalDate;

/**
 * Represents a day of duty.
 * @author desrepair
 */
public class DutyDay {
    private LocalDate date;
    private int pointValue;
    private Ra assigned;
    
    /**
     * Creates a DutyDay object representing the specified date.
     * @param d Date of the duty day.
     */
    public DutyDay(LocalDate d) {
        date = d;
        pointValue = 1;
        assigned = null;
    }
    
    /**
     * Sets the RA on duty for the duty day.
     * @param onDuty RA on duty.
     */
    public void assignRa(Ra onDuty) {
        assigned = onDuty;
    }
    
    /**
     * Sets the point value of the duty day.
     * @param value Point value to set to.
     */
    public void setPointValue(int value) {
        pointValue = value;
    }
    
    /**
     * Returns the date of the duty day.
     * @return Date of the duty day.
     */
    public LocalDate getDate() {
        return date;
    }
    
    /**
     * Returns the point value of the duty day.
     * @return Point value of the duty day.
     */
    public int getPointValue() {
        return pointValue;
    }
    
    /**
     * Returns the RA assigned to duty on this duty day.
     * @return RA assigned on duty.
     */
    public Ra getRaOnDuty() {
        return assigned;
    }
}
