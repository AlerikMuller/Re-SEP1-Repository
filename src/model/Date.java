package model;

import java.time.LocalDate;

public class Date implements Comparable<Date> {
    private int day;
    private int month;
    private int year;

    public Date(int day, int month, int year) {
        setDate(day, month, year);
    }

    public void setDay(int day) {
        setDate(day, this.month == 0 ? 1 : this.month, this.year == 0 ? 1 : this.year);
    }

    public int getDay() {
        return day;
    }

    public void setMonth(int month) {
        setDate(this.day == 0 ? 1 : this.day, month, this.year == 0 ? 1 : this.year);
    }

    public int getMonth() {
        return month;
    }

    public void setYear(int year) {
        setDate(this.day == 0 ? 1 : this.day, this.month == 0 ? 1 : this.month, year);
    }

    public int getYear() {
        return year;
    }

    public void setDate(int day, int month, int year) {
        validateDate(day, month, year);
        this.day = day;
        this.month = month;
        this.year = year;
    }

    private void validateDate(int day, int month, int year) {
        try {
            LocalDate.of(year, month, day);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date.");
        }
    }

    private LocalDate toLocalDate() {
        return LocalDate.of(year, month, day);
    }

    public boolean isBefore(Date other) {
        if (other == null) {
            throw new IllegalArgumentException("Other date cannot be empty.");
        }
        return this.toLocalDate().isBefore(other.toLocalDate());
    }

    public boolean isAfter(Date other) {
        if (other == null) {
            throw new IllegalArgumentException("Other date cannot be empty.");
        }
        return this.toLocalDate().isAfter(other.toLocalDate());
    }

    public boolean equals(Date other) {
        return other != null &&
                this.day == other.day &&
                this.month == other.month &&
                this.year == other.year;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Date other)) {
            return false;
        }
        return equals(other);
    }

    @Override
    public int hashCode() {
        return toLocalDate().hashCode();
    }

    @Override
    public int compareTo(Date other) {
        if (other == null) {
            throw new IllegalArgumentException("Other date cannot be empty.");
        }
        return this.toLocalDate().compareTo(other.toLocalDate());
    }

    @Override
    public String toString() {
        return String.format("%02d/%02d/%04d", day, month, year);
    }
}