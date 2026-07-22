package model;

public class TimeInterval {
    private Time startTime;
    private Time endTime;

    public TimeInterval(Time startTime, Time endTime) {
        setTimeInterval(startTime, endTime);
    }

    public void setStartTime(Time time) {
        setTimeInterval(time, this.endTime == null ? time : this.endTime);
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setEndTime(Time time) {
        setTimeInterval(this.startTime == null ? time : this.startTime, time);
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setTimeInterval(Time startTime, Time endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start time and end time cannot be empty.");
        }
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }

        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean isValid() {
        return startTime != null && endTime != null && startTime.isBefore(endTime);
    }

    public boolean overlaps(TimeInterval other) {
        if (other == null) {
            throw new IllegalArgumentException("Other time interval cannot be empty.");
        }

        return this.startTime.isBefore(other.endTime) &&
                other.startTime.isBefore(this.endTime);
    }

    @Override
    public String toString() {
        return startTime + " - " + endTime;
    }
}