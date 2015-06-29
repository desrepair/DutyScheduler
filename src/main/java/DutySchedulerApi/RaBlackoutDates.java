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
public class RaBlackoutDates {
    private String name;
    private LocalDate[] blackoutDates;
    
    private RaBlackoutDates() {
        
    }

    public String getName() {
        return name;
    }

    public LocalDate[] getBlackoutDates() {
        return blackoutDates;
    }
}
