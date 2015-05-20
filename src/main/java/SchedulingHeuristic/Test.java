package SchedulingHeuristic;

import java.time.LocalDate;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        DutyCalendar cal = new DutyCalendar(
                LocalDate.of(2015, 05, 19),
                LocalDate.of(2015, 05, 26),
                2);
        ArrayList<LocalDate> micksBlackout = new ArrayList<LocalDate>();
        micksBlackout.add(LocalDate.of(2015, 05, 19));
        micksBlackout.add(LocalDate.of(2015, 05, 20));
        micksBlackout.add(LocalDate.of(2015, 05, 21));
        cal.addRa("Mick", micksBlackout);
        cal.addRa("Wenlan's Chosen", new ArrayList<LocalDate>());
        cal.addRa("Yujin's Mom", new ArrayList<LocalDate>());
        cal.assignDuty();
        System.out.println(cal);
    }
}
