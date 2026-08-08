package model;

/**
 * Represents a time of day used for trips and chauffeur schedules.
 * The class stores hours, minutes, and seconds while validating the
 * values and providing chronological comparison operations between times.
 *
 * @author Alerik Muller
 * @version 1.0
 */
public class Time implements Comparable<Time> {
    private int hour;
    private int minute;
    private int second;

    /**
     * Creates a time from the given hour, minute, and second.
     *
     * @param hour the hour value
     * @param minute the minute value
     * @param second the second value
     */
    public Time(int hour, int minute, int second) {
        setTime(hour, minute, second);
    }

    /**
     * Sets the hour while retaining and revalidating the minute and second.
     *
     * @param hour the new hour value
     */
    public void setHour(int hour) {
        setTime(hour, this.minute, this.second);
    }

    /**
     * Returns the stored hour.
     *
     * @return the hour value
     */
    public int getHour() {
        return hour;
    }

    /**
     * Sets the minute while retaining and revalidating the hour and second.
     *
     * @param minute the new minute value
     */
    public void setMinute(int minute) {
        setTime(this.hour, minute, this.second);
    }

    /**
     * Returns the stored minute.
     *
     * @return the minute value
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Sets the second while retaining and revalidating the hour and minute.
     *
     * @param second the new second value
     */
    public void setSecond(int second) {
        setTime(this.hour, this.minute, second);
    }

    /**
     * Returns the stored second.
     *
     * @return the second value
     */
    public int getSecond() {
        return second;
    }

    /**
     * Sets all time values after validating the twenty-four-hour ranges.
     *
     * @param hour the hour value
     * @param minute the minute value
     * @param second the second value
     */
    public void setTime(int hour, int minute, int second) {
        validateTime(hour, minute, second);
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    /**
     * Validates that hour, minute, and second values are within valid ranges.
     *
     * @param hour the hour value to validate
     * @param minute the minute value to validate
     * @param second the second value to validate
     * @throws IllegalArgumentException if any value is outside its valid range
     */
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

    /**
     * Converts the stored time into its total number of seconds after midnight.
     *
     * @return the total number of seconds represented by this time
     */
    private int toSeconds() {
        return hour * 3600 + minute * 60 + second;
    }

    /**
     * Checks whether this time occurs before another time.
     *
     * @param other the time to compare with
     * @return {@code true} if this time occurs before the other time
     * @throws IllegalArgumentException if the other time is {@code null}
     */
    public boolean isBefore(Time other) {
        if (other == null) {
            throw new IllegalArgumentException("Other time cannot be empty.");
        }
        return this.toSeconds() < other.toSeconds();
    }

    /**
     * Checks whether this time occurs after another time.
     *
     * @param other the time to compare with
     * @return {@code true} if this time occurs after the other time
     * @throws IllegalArgumentException if the other time is {@code null}
     */
    public boolean isAfter(Time other) {
        if (other == null) {
            throw new IllegalArgumentException("Other time cannot be empty.");
        }
        return this.toSeconds() > other.toSeconds();
    }

    /**
     * Compares this time directly with another {@code Time}.
     *
     * @param other the time to compare with
     * @return {@code true} if both times contain identical values
     */
    public boolean equals(Time other) {
        return other != null &&
                this.hour == other.hour &&
                this.minute == other.minute &&
                this.second == other.second;
    }

    /**
     * Compares this time with another object for equality.
     *
     * @param obj the object to compare with
     * @return {@code true} if the object represents the same time
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Time other)) {
            return false;
        }
        return equals(other);
    }

    /**
     * Returns a hash code based on the number of seconds represented.
     *
     * @return the hash code for this time
     */
    @Override
    public int hashCode() {
        return toSeconds();
    }

    /**
     * Compares this time chronologically with another time.
     *
     * @param other the time to compare with
     * @return a negative value, zero, or positive value depending on chronological order
     * @throws IllegalArgumentException if the other time is {@code null}
     */
    @Override
    public int compareTo(Time other) {
        if (other == null) {
            throw new IllegalArgumentException("Other time cannot be empty.");
        }
        return Integer.compare(this.toSeconds(), other.toSeconds());
    }

    /**
     * Returns the time formatted using twenty-four-hour notation.
     *
     * @return the time formatted as {@code HH:mm:ss}
     */
    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
}