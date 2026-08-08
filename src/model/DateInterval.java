package model;

/**
 * Represents a period between a starting date and an ending date.
 * The class validates the chronological order of the dates and provides
 * operations for checking future dates and overlapping date intervals.
 *
 * @author Alerik Muller
 * @version 1.0
 */
public class DateInterval {
    private Date startDate;
    private Date endDate;

    /**
     * Creates a date interval with the given starting and ending dates.
     *
     * @param startDate the first date of the interval
     * @param endDate the final date of the interval
     */
    public DateInterval(Date startDate, Date endDate) {
        setDateInterval(startDate, endDate);
    }

    /**
     * Sets the starting date while preserving and validating the ending date.
     *
     * @param date the new starting date
     */
    public void setStartDate(Date date) {
        setDateInterval(date, this.endDate == null ? date : this.endDate);
    }

    /**
     * Returns the starting date of the interval.
     *
     * @return the starting date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the ending date while preserving and validating the starting date.
     *
     * @param date the new ending date
     */
    public void setEndDate(Date date) {
        setDateInterval(this.startDate == null ? date : this.startDate, date);
    }

    /**
     * Returns the ending date of the interval.
     *
     * @return the ending date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets both dates and ensures the starting date does not follow the ending date.
     *
     * @param startDate the starting date
     * @param endDate the ending date
     * @throws IllegalArgumentException if either date is null or the order is invalid
     */
    public void setDateInterval(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be empty.");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Checks whether the interval currently contains two valid chronological dates.
     *
     * @return {@code true} if the interval is valid, otherwise {@code false}
     */
    public boolean isValid() {
        return startDate != null && endDate != null && !startDate.isAfter(endDate);
    }

    /**
     * Checks whether the interval begins after the supplied current date.
     *
     * @param currentDate the date used as the present reference
     * @return {@code true} if the interval starts after the supplied date
     * @throws IllegalArgumentException if the current date is {@code null}
     */
    public boolean isFuture(Date currentDate) {
        if (currentDate == null) {
            throw new IllegalArgumentException("Current date cannot be empty.");
        }
        return startDate.isAfter(currentDate);
    }

    /**
     * Checks whether this interval shares at least one date with another interval.
     *
     * @param other the date interval to compare with
     * @return {@code true} if the two date intervals overlap
     * @throws IllegalArgumentException if the other interval is {@code null}
     */
    public boolean overlaps(DateInterval other) {
        if (other == null) {
            throw new IllegalArgumentException("Other date interval cannot be empty.");
        }

        return !this.endDate.isBefore(other.startDate) &&
                !this.startDate.isAfter(other.endDate);
    }

    /**
     * Returns the starting and ending dates as a readable interval.
     *
     * @return a string representation of the date interval
     */
    @Override
    public String toString() {
        return startDate + " - " + endDate;
    }
}