package model;

import java.time.LocalDate;

/**
 * Represents a calendar date used throughout the trip planning system.
 * The class stores day, month, and year values and provides validation
 * and comparison operations for working with dates chronologically.
 *
 * @author Alerik Muller
 * @version 1.0
 */
public class Date implements Comparable<Date> {
    private int day;
    private int month;
    private int year;

    /**
     * Creates a date from the given day, month, and year.
     *
     * @param day the day of the month
     * @param month the month of the year
     * @param year the year
     */
    public Date(int day, int month, int year) {
        setDate(day, month, year);
    }

    /**
     * Sets the day while retaining and revalidating the current month and year.
     *
     * @param day the new day value
     */
    public void setDay(int day) {
        setDate(day, this.month == 0 ? 1 : this.month, this.year == 0 ? 1 : this.year);
    }

    /**
     * Returns the stored day of the month.
     *
     * @return the day value
     */
    public int getDay() {
        return day;
    }

    /**
     * Sets the month while retaining and revalidating the current day and year.
     *
     * @param month the new month value
     */
    public void setMonth(int month) {
        setDate(this.day == 0 ? 1 : this.day, month, this.year == 0 ? 1 : this.year);
    }

    /**
     * Returns the stored month of the year.
     *
     * @return the month value
     */
    public int getMonth() {
        return month;
    }

    /**
     * Sets the year while retaining and revalidating the current day and month.
     *
     * @param year the new year value
     */
    public void setYear(int year) {
        setDate(this.day == 0 ? 1 : this.day, this.month == 0 ? 1 : this.month, year);
    }

    /**
     * Returns the stored year.
     *
     * @return the year value
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets all date values after validating that they form a real calendar date.
     *
     * @param day the day of the month
     * @param month the month of the year
     * @param year the year
     */
    public void setDate(int day, int month, int year) {
        validateDate(day, month, year);
        this.day = day;
        this.month = month;
        this.year = year;
    }

    /**
     * Validates a date by attempting to create an equivalent {@link LocalDate}.
     *
     * @param day the day to validate
     * @param month the month to validate
     * @param year the year to validate
     * @throws IllegalArgumentException if the date values are invalid
     */
    private void validateDate(int day, int month, int year) {
        try {
            LocalDate.of(year, month, day);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date.");
        }
    }

    /**
     * Converts this model date into a Java {@link LocalDate}.
     *
     * @return the equivalent {@code LocalDate}
     */
    private LocalDate toLocalDate() {
        return LocalDate.of(year, month, day);
    }

    /**
     * Converts this model date into a Java {@link LocalDate}.
     *
     * @return the equivalent {@code LocalDate}
     */
    public boolean isBefore(Date other) {
        if (other == null) {
            throw new IllegalArgumentException("Other date cannot be empty.");
        }
        return this.toLocalDate().isBefore(other.toLocalDate());
    }

    /**
     * Checks whether this date occurs after another date.
     *
     * @param other the date to compare with
     * @return {@code true} if this date occurs after the other date
     * @throws IllegalArgumentException if the other date is {@code null}
     */
    public boolean isAfter(Date other) {
        if (other == null) {
            throw new IllegalArgumentException("Other date cannot be empty.");
        }
        return this.toLocalDate().isAfter(other.toLocalDate());
    }

    /**
     * Compares this date directly with another {@code Date}.
     *
     * @param other the date to compare with
     * @return {@code true} if both dates contain the same values
     */
    public boolean equals(Date other) {
        return other != null &&
                this.day == other.day &&
                this.month == other.month &&
                this.year == other.year;
    }

    /**
     * Compares this date with another object for equality.
     *
     * @param obj the object to compare with
     * @return {@code true} if the object represents the same date
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Date other)) {
            return false;
        }
        return equals(other);
    }

    /**
     * Returns a hash code based on the equivalent {@link LocalDate}.
     *
     * @return the hash code for this date
     */
    @Override
    public int hashCode() {
        return toLocalDate().hashCode();
    }

    /**
     * Compares this date chronologically with another date.
     *
     * @param other the date to compare with
     * @return a negative value, zero, or positive value depending on chronological order
     * @throws IllegalArgumentException if the other date is {@code null}
     */
    @Override
    public int compareTo(Date other) {
        if (other == null) {
            throw new IllegalArgumentException("Other date cannot be empty.");
        }
        return this.toLocalDate().compareTo(other.toLocalDate());
    }

    /**
     * Returns the date formatted as day, month, and year.
     *
     * @return the date formatted as {@code DD/MM/YYYY}
     */
    @Override
    public String toString() {
        return String.format("%02d/%02d/%04d", day, month, year);
    }
}