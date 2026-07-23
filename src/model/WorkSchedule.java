package model;

public class WorkSchedule {
    private String day;
    private String status;
    private DateInterval dateInterval;
    private TimeInterval timeInterval;

    public WorkSchedule(String day, String status, DateInterval dateInterval) {
        setDay(day);
        setStatus(status);
        setDateInterval(dateInterval);
    }

    public WorkSchedule(String day, String status, DateInterval dateInterval, TimeInterval timeInterval) {
        this(day, status, dateInterval);
        setTimeInterval(timeInterval);
    }

    public void setDay(String day) {
        if (day == null || day.trim().isEmpty()) {
            throw new IllegalArgumentException("Day cannot be empty.");
        }
        if(day.equals("Sunday") || day.equals("Monday") || day.equals("Tuesday") || day.equals("Wednesday") ||  day.equals("Thursday") || day.equals("Friday") || day.equals("Saturday"))
        {
            this.day = day.trim();
        }
    }

    public String getDay() {
        return day;
    }

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

    public String getStatus() {
        return status;
    }

    public void setDateInterval(DateInterval dateInterval) {
        if (dateInterval == null) {
            throw new IllegalArgumentException("Date interval cannot be empty.");
        }
        this.dateInterval = dateInterval;
    }

    public DateInterval getDateInterval() {
        return dateInterval;
    }

    public void setTimeInterval(TimeInterval timeInterval) {
        if (timeInterval == null) {
            throw new IllegalArgumentException("Time interval cannot be empty.");
        }
        this.timeInterval = timeInterval;
    }

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

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