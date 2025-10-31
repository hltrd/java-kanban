package main.model;

import java.util.Objects;

public class TimeOfDay implements Comparable<TimeOfDay> {
    private int hours;
    private int minutes;

    public TimeOfDay(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() { return hours; }
    public int getMinutes() { return minutes; }

    @Override
    public int compareTo(TimeOfDay other) {
        if (this.hours != other.hours) {
            return this.hours - other.hours;
        }
        return this.minutes - other.minutes;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TimeOfDay)) return false;
        TimeOfDay that = (TimeOfDay) obj;
        return hours == that.hours && minutes == that.minutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours, minutes);
    }
}
