package SchedulingHeuristic;

import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;

/**
 * Represents the duty calendar.
 * @author desrepair
 */
public class DutyCalendar {
    private ArrayList<Ra> raList;
    private ArrayList<DutyDay> dutyCalendar;
    public static LocalDate currentDay;
    
    /**
     * Constructor
     * @param start Start date of the duty calendar, inclusive.
     * @param end End date of the duty calendar, inclusive.
     */
    public DutyCalendar(LocalDate start, LocalDate end) {
        raList = new ArrayList<Ra>();
        dutyCalendar = new ArrayList<DutyDay>();
        currentDay = start;
        
        //Populate days.
        while (!start.equals(end)) {
            dutyCalendar.add(new DutyDay(start));
            start = start.plusDays(1);
        }
    }
    
    /**
     * Adds an RA to the duty roster.
     * @param name Name of the RA to add to the duty roster.
     * @param blackoutDates Arraylist of blackout dates for the RA.
     */
    public void addRa(String name, ArrayList<LocalDate> blackoutDates) {
        raList.add(new Ra(name, blackoutDates));
    }
    
    /**
     * Sets the point value of each duty day.
     * @param map Day-Value mapping.
     */
    public void setDayValues(HashMap<LocalDate, Integer> map) {
        for (LocalDate key : map.keySet()) {
            DutyDay temp = getDutyDate(key);
            if (temp == null) {
                return;
            }
            temp.setPointValue(map.get(key));
        }
    }
    
    /**
     * Retrieves the DutyDay corresponding to the specified date.
     * @param toGet Date of the duty day to get.
     * @return DutyDay object corresponding to the specified date in the duty calendar.
     */
    private DutyDay getDutyDate(LocalDate toGet) {
        for (DutyDay day : dutyCalendar) {
            if (day.getDate().equals(toGet)) {
                return day;
            }
        }
        return null;
    }
    
    /**
     * Given the set of RAs and duty days, assign RAs to duty days.
     */
    public void assignDuty() {
        for (DutyDay day : dutyCalendar) {
            currentDay = day.getDate();
            double min = Double.MAX_VALUE;
            Ra toAssign = null;
            for (Ra ra : raList) {
                double value = ra.calculateDayWorth(currentDay);
                if (value < min) {
                    min = value;
                    toAssign = ra;
                }
            }
            toAssign.assignDay(day.getDate(), day.getPointValue());
            day.assignRa(toAssign);
            
        }
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (DutyDay day : dutyCalendar) {
            result.append(day.getDate());
            result.append(": ");
            result.append(day.getRaOnDuty());
            result.append("\n");
        }
        return result.toString();
    }
}
