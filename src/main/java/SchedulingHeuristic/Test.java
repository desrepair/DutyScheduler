package SchedulingHeuristic;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        DutyCalendar cal = new DutyCalendar(
                LocalDate.of(2015, 05, 19),
                LocalDate.of(2015, 05, 26),
                1);
        HashMap<LocalDate, Double> points = new HashMap<>();
        points.put(LocalDate.of(2015, 05, 19), 1.0);
        points.put(LocalDate.of(2015, 05, 20), 1.0);
        points.put(LocalDate.of(2015, 05, 21), 1.5);
        points.put(LocalDate.of(2015, 05, 22), 2.0);
        points.put(LocalDate.of(2015, 05, 23), 2.0);
        points.put(LocalDate.of(2015, 05, 24), 1.0);
        points.put(LocalDate.of(2015, 05, 25), 1.0);
        points.put(LocalDate.of(2015, 05, 26), 1.0);
        cal.setDayValues(points);
        ArrayList<LocalDate> micksBlackout = new ArrayList<LocalDate>();
        micksBlackout.add(LocalDate.of(2015, 05, 20));
        cal.addRa("Mick", micksBlackout);
        cal.addRa("Wenlan's Chosen", new ArrayList<LocalDate>());
        cal.addRa("Yujin's Mom", new ArrayList<LocalDate>());
        cal.assignDuty();
        System.out.println(cal);
    }
}
