/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DutySchedulerApi;

import java.time.LocalDate;

/**
 * Represents a calendar creation request.
 * @author desrepair
 */
public class CalendarCreationRequest {
    
    private String calendarName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int blockSize;
    private int rasPerBlock;
    private DayPoints[] pointsPerDay;
    private RaBlackoutDates[] ras;
    
    
    private CalendarCreationRequest() {
        
    }
    
    public String getCalendarName() {
        return calendarName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getRasPerBlock() {
        return rasPerBlock;
    }

    public DayPoints[] getPointsPerDay() {
        return pointsPerDay;
    }

    public RaBlackoutDates[] getRas() {
        return ras;
    }
}
