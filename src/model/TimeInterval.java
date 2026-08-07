package model;

//Represents daily time ranges and supports overlap detection across midnight.
public class TimeInterval
{
    private Time startTime;
    private Time endTime;

    public TimeInterval(Time startTime, Time endTime)
    {
        setTimeInterval(startTime, endTime);
    }

    public void setStartTime(Time time)
    {
        if (time == null)
        {
            throw new IllegalArgumentException("Start time cannot be empty.");
        }

        this.startTime = time;
    }

    public Time getStartTime()
    {
        return startTime;
    }

    public void setEndTime(Time time)
    {
        if (time == null)
        {
            throw new IllegalArgumentException("End time cannot be empty.");
        }

        this.endTime = time;
    }

    public Time getEndTime()
    {
        return endTime;
    }

    //Requires both time boundaries before storing the complete interval.
    public void setTimeInterval(Time startTime, Time endTime)
    {
        if (startTime == null || endTime == null)
        {
            throw new IllegalArgumentException("Start time and end time cannot be empty.");
        }

        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean isValid()
    {
        return startTime != null && endTime != null;
    }

    //Compares split time segments to detect shared occupied periods.
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

    //Splits overnight intervals into comparable segments around midnight.
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

    private int toSeconds(Time time)
    {
        return time.getHour() * 3600 + time.getMinute() * 60 + time.getSecond();
    }

    @Override
    public String toString()
    {
        return startTime + " - " + endTime;
    }
}