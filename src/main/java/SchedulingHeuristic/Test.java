package SchedulingHeuristic;

import java.time.LocalDate;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        DutyCalendar cal = new DutyCalendar(
                LocalDate.of(2015, 05, 19),
                LocalDate.of(2015, 05, 26));
        cal.addRa("Mick", new ArrayList<LocalDate>());
        cal.addRa("Wenlan's Chosen", new ArrayList<LocalDate>());
        cal.addRa("Yujin's Mom", new ArrayList<LocalDate>());
        cal.assignDuty();
        System.out.println(cal);
    }
}
