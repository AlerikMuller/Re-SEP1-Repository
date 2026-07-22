package model;

public class DateInterval {
    private Date startDate;
    private Date endDate;

    public DateInterval(Date startDate, Date endDate) {
        setDateInterval(startDate, endDate);
    }

    public void setStartDate(Date date) {
        setDateInterval(date, this.endDate == null ? date : this.endDate);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setEndDate(Date date) {
        setDateInterval(this.startDate == null ? date : this.startDate, date);
    }

    public Date getEndDate() {
        return endDate;
    }

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

    public boolean isValid() {
        return startDate != null && endDate != null && !startDate.isAfter(endDate);
    }

    public boolean isFuture(Date currentDate) {
        if (currentDate == null) {
            throw new IllegalArgumentException("Current date cannot be empty.");
        }
        return startDate.isAfter(currentDate);
    }

    public boolean overlaps(DateInterval other) {
        if (other == null) {
            throw new IllegalArgumentException("Other date interval cannot be empty.");
        }

        return !this.endDate.isBefore(other.startDate) &&
                !this.startDate.isAfter(other.endDate);
    }

    @Override
    public String toString() {
        return startDate + " - " + endDate;
    }
}