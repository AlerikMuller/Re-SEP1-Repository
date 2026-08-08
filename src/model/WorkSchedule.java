package model;

/**
 * Represents a work schedule belonging to a registered chauffeur.
 * The class stores the scheduled day, current schedule status, date
 * interval, and optional time interval used when checking availability.
 *
 * @author Alerik Muller
 * @version 1.0
 */
public class WorkSchedule {
    private String day;
    private String status;
    private DateInterval dateInterval;
    private TimeInterval timeInterval;

    /**
     * Creates a work schedule containing a day, status, and date interval.
     *
     * @param day the weekday of the schedule
     * @param status the schedule status
     * @param dateInterval the date interval covered by the schedule
     */
    public WorkSchedule(String day, String status, DateInterval dateInterval) {
        setDay(day);
        setStatus(status);
        setDateInterval(dateInterval);
    }

    /**
     * Creates a work schedule containing both date and time intervals.
     *
     * @param day the weekday of the schedule
     * @param status the schedule status
     * @param dateInterval the date interval covered by the schedule
     * @param timeInterval the time interval covered by the schedule
     */
    public WorkSchedule(String day, String status, DateInterval dateInterval, TimeInterval timeInterval) {
        this(day, status, dateInterval);
        setTimeInterval(timeInterval);
    }

    /**
     * Sets the weekday when it matches one of the seven supported days.
     *
     * @param day the weekday to store
     * @throws IllegalArgumentException if the day is null or empty
     */
    public void setDay(String day) {
        if (day == null || day.trim().isEmpty()) {
            throw new IllegalArgumentException("Day cannot be empty.");
        }
        if(day.equals("Sunday") || day.equals("Monday") || day.equals("Tuesday") || day.equals("Wednesday") ||  day.equals("Thursday") || day.equals("Friday") || day.equals("Saturday"))
        {
            this.day = day.trim();
        }
    }

    /**
     * Returns the weekday stored for the schedule.
     *
     * @return the scheduled weekday
     */
    public String getDay() {
        return day;
    }

    /**
     * Sets the schedule status when it is either Active or OFF.
     *
     * @param status the schedule status to store
     * @throws IllegalArgumentException if the status is null or empty
     */
    public void setStatus(String status)
    {
        if (status == null || status.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "Work schedule status cannot be empty.");
        }
        if (status.equals("Active") || status.equals("OFF"))
        {
            this.status = status.trim();
        }
    }

    /**
     * Returns the current status of the schedule.
     *
     * @return the schedule status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the date interval covered by the work schedule.
     *
     * @param dateInterval the date interval to store
     * @throws IllegalArgumentException if the interval is {@code null}
     */
    public void setDateInterval(DateInterval dateInterval) {
        if (dateInterval == null) {
            throw new IllegalArgumentException("Date interval cannot be empty.");
        }
        this.dateInterval = dateInterval;
    }

    /**
     * Returns the date interval covered by the schedule.
     *
     * @return the schedule date interval
     */
    public DateInterval getDateInterval() {
        return dateInterval;
    }

    /**
     * Sets the time interval covered by the work schedule.
     *
     * @param timeInterval the time interval to store
     * @throws IllegalArgumentException if the interval is {@code null}
     */
    public void setTimeInterval(TimeInterval timeInterval) {
        if (timeInterval == null) {
            throw new IllegalArgumentException("Time interval cannot be empty.");
        }
        this.timeInterval = timeInterval;
    }

    /**
     * Returns the optional time interval covered by the schedule.
     *
     * @return the schedule time interval, or {@code null} if none exists
     */
    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    /**
     * Checks whether this schedule makes the chauffeur available for the
     * requested date and time intervals.
     *
     * @param requestedDateInterval the requested date interval
     * @param requestedTimeInterval the requested time interval, or {@code null}
     * @return {@code true} if this schedule covers the requested period
     * @throws IllegalArgumentException if the requested date interval is {@code null}
     */
    public boolean isAvailableFor(DateInterval requestedDateInterval, TimeInterval requestedTimeInterval) {
        if (requestedDateInterval == null) {
            throw new IllegalArgumentException("Requested date interval cannot be empty.");
        }

        boolean activeStatus = status.equalsIgnoreCase("ACTIVE") ||
                status.equalsIgnoreCase("AVAILABLE");

        if (!activeStatus) {
            return false;
        }

        boolean dateMatches = dateInterval.overlaps(requestedDateInterval);

        if (!dateMatches) {
            return false;
        }

        if (timeInterval == null || requestedTimeInterval == null) {
            return true;
        }

        return timeInterval.overlaps(requestedTimeInterval);
    }

    /**
     * Returns a string containing the stored work schedule information.
     *
     * @return a string representation of the work schedule
     */
    @Override
    public String toString() {
        return "WorkSchedule{" +
                "day='" + day + '\'' +
                ", status='" + status + '\'' +
                ", dateInterval=" + dateInterval + '\'' +
                ", timeInterval=" + timeInterval + '\'' +
                '}';
    }
}