package model;

/**
 * Represents a period between a starting time and an ending time.
 * The class stores both boundaries and provides overlap detection,
 * including support for time intervals that continue across midnight.
 *
 * @author Alerik Muller
 * @version 1.0
 */
public class TimeInterval
{
    private Time startTime;
    private Time endTime;

    /**
     * Creates a time interval with the given starting and ending times.
     *
     * @param startTime the beginning of the interval
     * @param endTime the end of the interval
     */
    public TimeInterval(Time startTime, Time endTime)
    {
        setTimeInterval(startTime, endTime);
    }

    /**
     * Sets the starting time of the interval.
     *
     * @param time the new starting time
     * @throws IllegalArgumentException if the time is {@code null}
     */
    public void setStartTime(Time time)
    {
        if (time == null)
        {
            throw new IllegalArgumentException("Start time cannot be empty.");
        }

        this.startTime = time;
    }

    /**
     * Returns the starting time of the interval.
     *
     * @return the starting time
     */
    public Time getStartTime()
    {
        return startTime;
    }

    /**
     * Sets the ending time of the interval.
     *
     * @param time the new ending time
     * @throws IllegalArgumentException if the time is {@code null}
     */
    public void setEndTime(Time time)
    {
        if (time == null)
        {
            throw new IllegalArgumentException("End time cannot be empty.");
        }

        this.endTime = time;
    }

    /**
     * Returns the ending time of the interval.
     *
     * @return the ending time
     */
    public Time getEndTime()
    {
        return endTime;
    }

    /**
     * Sets both time boundaries after ensuring neither value is {@code null}.
     *
     * @param startTime the beginning of the interval
     * @param endTime the end of the interval
     * @throws IllegalArgumentException if either time is {@code null}
     */
    public void setTimeInterval(Time startTime, Time endTime)
    {
        if (startTime == null || endTime == null)
        {
            throw new IllegalArgumentException("Start time and end time cannot be empty.");
        }

        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Checks whether both boundaries of the interval have been assigned.
     *
     * @return {@code true} if both start and end times exist
     */
    public boolean isValid()
    {
        return startTime != null && endTime != null;
    }

    /**
     * Checks whether this time interval overlaps another interval.
     * Intervals that pass midnight are divided into separate ranges.
     *
     * @param other the interval to compare with
     * @return {@code true} if the two intervals overlap
     * @throws IllegalArgumentException if the other interval is {@code null}
     */
    public boolean overlaps(TimeInterval other)
    {
        if (other == null)
        {
            throw new IllegalArgumentException("Other time interval cannot be empty.");
        }

        int[][] firstRanges = toRanges(this);
        int[][] secondRanges = toRanges(other);

        for (int[] firstRange : firstRanges)
        {
            for (int[] secondRange : secondRanges)
            {
                if (firstRange[0] < secondRange[1] && secondRange[0] < firstRange[1])
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Converts an interval into one or two second-based ranges.
     * Overnight intervals are split at midnight for easier comparison.
     *
     * @param interval the time interval to convert
     * @return an array containing the comparable time ranges
     */
    private int[][] toRanges(TimeInterval interval)
    {
        int start = toSeconds(interval.startTime);
        int end = toSeconds(interval.endTime);

        if (start < end)
        {
            return new int[][]{{start, end}};
        }

        if (start > end)
        {
            return new int[][]{{start, 86400}, {0, end}};
        }

        return new int[][]{{0, 86400}};
    }

    /**
     * Converts a time into its total number of seconds after midnight.
     *
     * @param time the time to convert
     * @return the total number of seconds represented by the time
     */
    private int toSeconds(Time time)
    {
        return time.getHour() * 3600 + time.getMinute() * 60 + time.getSecond();
    }

    /**
     * Returns the starting and ending times as a readable interval.
     *
     * @return a string representation of the time interval
     */
    @Override
    public String toString()
    {
        return startTime + " - " + endTime;
    }
}