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
    private ArrayList<DutyBlock> dutyCalendar;
    private int blockSize;
    private int rasPerBlock;

    public static DutyBlock currentBlock;
    public static int numberOfRAs;
    
    /**
     * Constructor
     * @param start Start date of the duty calendar, inclusive.
     * @param end End date of the duty calendar, inclusive.
     * @param bSize Length of duty blocks in days.
     * @param rNum Number of RAs to assign to a duty block.
     */
    public DutyCalendar(LocalDate start, LocalDate end, int bSize, int rNum) {
        raList = new ArrayList<>();
        dutyCalendar = new ArrayList<>();
        blockSize = bSize;
        numberOfRAs = 0;
        rasPerBlock = rNum;
        
        //Populate days.
        while (!start.isAfter(end)) {
            dutyCalendar.add(new DutyBlock(start, blockSize));
            start = start.plusDays(bSize);
        }
        currentBlock = dutyCalendar.get(0);
    }
    
    /**
     * Adds an RA to the duty roster.
     * @param name Name of the RA to add to the duty roster.
     * @param blackoutDates Arraylist of blackout dates for the RA.
     */
    public void addRa(String name, ArrayList<LocalDate> blackoutDates) {
        raList.add(new Ra(name, blackoutDates));
        numberOfRAs++;
    }
    
    /**
     * Sets the point value of each duty day.
     * @param map Day-Value mapping.
     */
    public void setDayValues(HashMap<LocalDate, Double> map) {
        for (LocalDate key : map.keySet()) {
            DutyBlock temp = getDutyBlock(key);
            if (temp != null) {
                temp.addPointValue(map.get(key));
            }
        }
    }
    
    /**
     * Retrieves the DutyBlock corresponding to the specified date.
     * @param toGet Date of the duty day to get.
     * @return DutyBlock object corresponding to the specified date in the duty calendar.
     */
    private DutyBlock getDutyBlock(LocalDate toGet) {
        DutyBlock closestBlock = dutyCalendar.get(0);
        for (DutyBlock block : dutyCalendar) {
            if (block.getStartDate().equals(toGet)) {
                return block;
            }
            if (toGet.isAfter(block.getStartDate()) && toGet.isBefore(block.getEndDate().plusDays(1))) {
                return block;
            }
        }
        return null;
    }
    
    /**
     * Given the set of RAs and duty days, assign RAs to duty days.
     */
    public ArrayList<DutyBlock> assignDuty() {
        for (DutyBlock block : dutyCalendar) {
            currentBlock = block;
            ArrayList<Ra> assignedToday = new ArrayList<>();
            for (int numAssigned = 0; numAssigned < rasPerBlock; numAssigned++) {
                double min = Double.MAX_VALUE;
                Ra toAssign = null;
                for (Ra ra : raList) {
                    double value = ra.calculateDayWorth(block);
                    if (value < min && !assignedToday.contains(ra)) {
                        min = value;
                        toAssign = ra;
                    }
                }
                toAssign.assignDay(block.getStartDate(), block.getPointValue());
                block.assignRa(toAssign);
                assignedToday.add(toAssign);
            }
        }
        return dutyCalendar;
    }
    
    /**
     * Returns a String representation of RAs and the points assigned to them.
     * @return 
     */
    public String printRaPointValues() {
        StringBuilder result = new StringBuilder();
        for (Ra ra : raList) {
            result.append(ra);
            result.append(": ");
            result.append(String.valueOf(ra.getPointsTaken()));
            result.append("\n");
        }
        return result.toString();
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (DutyBlock day : dutyCalendar) {
            result.append(day.getStartDate());
            result.append(": ");
            for (Ra ra : day.getRasOnDuty()) {
                result.append(ra);
                result.append("; ");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
