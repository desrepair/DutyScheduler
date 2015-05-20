package SchedulingHeuristic;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents an RA.
 * @author desrepair
 */
public class Ra implements Comparable<Ra> {
    private String name;
    private double pointsTaken;
    private LocalDate lastDutyDay;
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
        lastDutyDay = DutyCalendar.currentDay;
        dutyDays = new ArrayList<>();
    }
    
    /**
     * Returns the days since the RA was last on duty. Default 14.
     * @return Days since RA last on duty.
     */
    private int daysSinceLastDuty() {
        if (lastDutyDay == null) {
            return 14;
        }
        return (int) lastDutyDay.until(DutyCalendar.currentDay, ChronoUnit.DAYS);
    }
    
    /**
     * Assigns the day for the RA to be on duty.
     * @param day A day the RA is on duty.
     * @param points Point value of the day of duty.
     */
    public void assignDay(LocalDate day, int points) {
        lastDutyDay = day;
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
     * @param d Date to determine RA's priority for.
     * @return RA's priority for the duty day.
     */
    public double calculateDayWorth(LocalDate d) {
        if (blackoutDates.contains(d)) {
            return Double.POSITIVE_INFINITY;
        }
        
        return pointsTaken + 0.01 * (14 - daysSinceLastDuty());
    }

    @Override
    public int compareTo(Ra o) {
        return (int) (calculateDayWorth(DutyCalendar.currentDay)
                - o.calculateDayWorth(DutyCalendar.currentDay));
    }
    
    @Override
    public String toString() {
        return name;
    }
}
