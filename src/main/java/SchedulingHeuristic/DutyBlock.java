package SchedulingHeuristic;

import java.time.LocalDate;

/**
 * Represents a block of duty.
 * @author desrepair
 */
public class DutyBlock {
    private LocalDate startDate;
    private LocalDate endDate; //Inclusive
    private int blockLength;
    private double pointValue;
    private Ra assigned;
    
    /**
     * Creates a DutyBlock object representing the specified startDate.
     * @param d Date of the duty day.
     */
    public DutyBlock(LocalDate d, int length) {
        startDate = d;
        blockLength = length;
        endDate = startDate.plusDays(length);
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
    public void setPointValue(double value) {
        pointValue = value;
    }
    
    /**
     * Increases the point value of the duty block.
     * @param value  Value to increase by.
     */
    public void addPointValue(double value) {
        pointValue += value;
    }
    
    /**
     * Returns the start date of the duty block.
     * @return Start date of the duty day.
     */
    public LocalDate getStartDate() {
        return startDate;
    }
    
    /**
     * Returns the end date of the duty block.
     * @return End date of the duty day.
     */
    public LocalDate getEndDate() {
        return endDate;
    }
    
    /**
     * Returns the point value of the duty block.
     * @return Point value of the duty day.
     */
    public double getPointValue() {
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
