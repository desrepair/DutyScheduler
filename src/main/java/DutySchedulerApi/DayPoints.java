/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DutySchedulerApi;

import java.time.LocalDate;

/**
 *
 * @author desrepair
 */
public class DayPoints {
    private LocalDate date;
    private double pointValue;
    
    private DayPoints() {
        
    }

    public LocalDate getDate() {
        return date;
    }

    public double getPointValue() {
        return pointValue;
    }
    
    
}
