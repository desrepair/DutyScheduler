package SchedulingHeuristic;

import java.time.LocalDate;

/**
 * Represents a block of duty.
 * @author desrepair
 */
public class DutyBlock {
    private LocalDate startDate;
    private int pointValue;
    private Ra assigned;
    
    /**
     * Creates a DutyBlock object representing the specified startDate.
     * @param d Date of the duty day.
     */
    public DutyBlock(LocalDate d) {
        startDate = d;
        pointValue = 0;
        assigned = null;
    }
    
    /**
     * Sets the RA on duty for the duty block.
     * @param onDuty RA on duty.
     */
    public void assignRa(Ra onDuty) {
        assigned = onDuty;
    }
    
    /**
     * Sets the point value of the duty block.
     * @param value Point value to set to.
     */
    public void setPointValue(int value) {
        pointValue = value;
    }
    
    /**
     * Increases the point value of the duty block.
     * @param value  Value to increase by.
     */
    public void addPointValue(int value) {
        pointValue += value;
    }
    
    /**
     * Returns the start date of the duty block.
     * @return Date of the duty day.
     */
    public LocalDate getDate() {
        return startDate;
    }
    
    /**
     * Returns the point value of the duty block.
     * @return Point value of the duty day.
     */
    public int getPointValue() {
        return pointValue;
    }
    
    /**
     * Returns the RA assigned to duty on this duty block.
     * @return RA assigned on duty.
     */
    public Ra getRaOnDuty() {
        return assigned;
    }
}
