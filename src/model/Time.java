package model;

public class Time implements Comparable<Time> {
    private int hour;
    private int minute;
    private int second;

    public Time(int hour, int minute, int second) {
        setTime(hour, minute, second);
    }

    public void setHour(int hour) {
        setTime(hour, this.minute, this.second);
    }

    public int getHour() {
        return hour;
    }

    public void setMinute(int minute) {
        setTime(this.hour, minute, this.second);
    }

    public int getMinute() {
        return minute;
    }

    public void setSecond(int second) {
        setTime(this.hour, this.minute, second);
    }

    public int getSecond() {
        return second;
    }

    public void setTime(int hour, int minute, int second) {
        validateTime(hour, minute, second);
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    private void validateTime(int hour, int minute, int second) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Hour must be between 0 and 23.");
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Minute must be between 0 and 59.");
        }
        if (second < 0 || second > 59) {
            throw new IllegalArgumentException("Second must be between 0 and 59.");
        }
    }

    private int toSeconds() {
        return hour * 3600 + minute * 60 + second;
    }

    public boolean isBefore(Time other) {
        if (other == null) {
            throw new IllegalArgumentException("Other time cannot be empty.");
        }
        return this.toSeconds() < other.toSeconds();
    }

    public boolean isAfter(Time other) {
        if (other == null) {
            throw new IllegalArgumentException("Other time cannot be empty.");
        }
        return this.toSeconds() > other.toSeconds();
    }

    public boolean equals(Time other) {
        return other != null &&
                this.hour == other.hour &&
                this.minute == other.minute &&
                this.second == other.second;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Time other)) {
            return false;
        }
        return equals(other);
    }

    @Override
    public int hashCode() {
        return toSeconds();
    }

    @Override
    public int compareTo(Time other) {
        if (other == null) {
            throw new IllegalArgumentException("Other time cannot be empty.");
        }
        return Integer.compare(this.toSeconds(), other.toSeconds());
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
}