package main;

import main.model.*;
import main.service.*;

import java.util.List;

public class App {
    public static void main(String[] args) {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Сидоров", "Василий", "Петрович");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.TUESDAY, new TimeOfDay(11, 0)));

        System.out.println("Расписание на понедельник:");
        for (TrainingSession s : timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY)) {
            System.out.println(" - " + s.getGroup().getTitle() + " в " + s.getTimeOfDay().getHours() + ":00");
        }

        System.out.println("\nКоличество тренировок по тренерам:");
        List<CounterOfTrainings> counters = timetable.getCountByCoaches();
        for (CounterOfTrainings c : counters) {
            System.out.println(c);
        }
    }
}
