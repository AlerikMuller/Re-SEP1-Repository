package model;

//Stores trip details, assigned resources, intervals, status, and customer.
public class Trip
{
    private String origin;
    private String destination;
    private String status;
    private Bus assignedBus;
    private Chauffeur assignedChauffeur;
    private DateInterval dateInterval;
    private TimeInterval timeInterval;
    private Customer customer;

    public Trip()
    {
    }

    public Trip(String origin, String destination, String status, Bus assignedBus, Chauffeur assignedChauffeur, DateInterval dateInterval, TimeInterval timeInterval)
    {
        setOrigin(origin);
        setDestination(destination);
        setStatus(status);
        assignBus(assignedBus);
        assignChauffeur(assignedChauffeur);
        setDateInterval(dateInterval);
        setTimeInterval(timeInterval);
    }

    public Trip(String origin, String destination, String status, DateInterval dateInterval, TimeInterval timeInterval)
    {
        setOrigin(origin);
        setDestination(destination);
        setStatus(status);
        setDateInterval(dateInterval);
        setTimeInterval(timeInterval);
    }

    public Trip(String origin, String destination, String status, Bus assignedBus, Chauffeur assignedChauffeur, DateInterval dateInterval, TimeInterval timeInterval, Customer customer)
    {
        this(origin, destination, status, assignedBus, assignedChauffeur, dateInterval, timeInterval);
        setCustomer(customer);
    }

    public void setOrigin(String originAddress)
    {
        if (originAddress == null || originAddress.trim().isEmpty())
        {
            throw new IllegalArgumentException("Origin address cannot be empty.");
        }

        this.origin = originAddress.trim();
    }

    public String getOrigin()
    {
        return origin;
    }

    public void setDestination(String destination)
    {
        if (destination == null || destination.trim().isEmpty())
        {
            throw new IllegalArgumentException("Destination address cannot be empty.");
        }

        this.destination = destination.trim();
    }

    public String getDestination()
    {
        return destination;
    }

    //Restricts trip status to the four supported workflow states.
    public void setStatus(String status)
    {
        if (status == null || status.trim().isEmpty())
        {
            throw new IllegalArgumentException("Trip status cannot be empty.");
        }

        if (!(status.equalsIgnoreCase("Not Started") || status.equalsIgnoreCase("Started") || status.equalsIgnoreCase("Cancelled") || status.equalsIgnoreCase("Ended")))
        {
            throw new IllegalArgumentException("Status must be either 'Not Started' or 'Started' or 'Cancelled' or 'Ended'.");
        }

        if (status.equalsIgnoreCase("Not Started"))
        {
            this.status = "Not Started";
        }
        else if (status.equalsIgnoreCase("Started"))
        {
            this.status = "Started";
        }
        else if (status.equalsIgnoreCase("Cancelled"))
        {
            this.status = "Cancelled";
        }
        else
        {
            this.status = "Ended";
        }
    }

    public String getStatus()
    {
        return status;
    }

    //Assigns a required bus after preventing null resource references.
    public void assignBus(Bus bus)
    {
        if (bus == null)
        {
            throw new IllegalArgumentException("Assigned Bus cannot be null.");
        }

        this.assignedBus = bus;
    }

    public boolean isBusAssigned()
    {
        return assignedBus != null;
    }

    public Bus getAssignedBus()
    {
        return assignedBus;
    }

    //Assigns a required chauffeur after preventing null resource references.
    public void assignChauffeur(Chauffeur chauffeur)
    {
        if (chauffeur == null)
        {
            throw new IllegalArgumentException("Assigned chauffeur cannot be empty.");
        }

        this.assignedChauffeur = chauffeur;
    }

    public Chauffeur getAssignedChauffeur()
    {
        return assignedChauffeur;
    }

    public void setDateInterval(DateInterval dateInterval)
    {
        if (dateInterval == null)
        {
            throw new IllegalArgumentException("Date interval cannot be empty.");
        }

        this.dateInterval = dateInterval;
    }

    public DateInterval getDateInterval()
    {
        return dateInterval;
    }

    public String getDateIntervalString()
    {
        return dateInterval == null ? "" : dateInterval.toString();
    }

    public void setTimeInterval(TimeInterval timeInterval)
    {
        if (timeInterval == null)
        {
            throw new IllegalArgumentException("Time interval cannot be empty.");
        }

        this.timeInterval = timeInterval;
    }

    public TimeInterval getTimeInterval()
    {
        return timeInterval;
    }

    public String getTimeIntervalString()
    {
        return timeInterval == null ? "" : timeInterval.toString();
    }

    public void setCustomer(String name, String phone)
    {
        this.customer = new Customer(name, phone);
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public void removeCustomer()
    {
        this.customer = null;
    }

    //Allows editing unless the trip currently has Started status.
    public boolean canBeEdited()
    {
        return status.equals("Not Started") || status.equals("Cancelled") || status.equals("Ended");
    }

    //Allows removal unless the trip currently has Started status.
    public boolean canBeRemoved()
    {
        return status.equals("Not Started") || status.equals("Cancelled") || status.equals("Ended");
    }

    public boolean hasStarted()
    {
        return status.equals("Started") || status.equals("Ended");
    }

    //Checks whether another trip overlaps both date and time.
    public boolean overlaps(Trip other)
    {
        if (other == null)
        {
            throw new IllegalArgumentException("Other trip cannot be empty.");
        }

        return this.dateInterval.overlaps(other.dateInterval) && this.timeInterval.overlaps(other.timeInterval);
    }

    @Override
    public String toString()
    {
        return "Trip{" +
                "origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", status='" + status + '\'' +
                ", assignedBus=" + assignedBus + '\'' +
                ", assignedChauffeur=" + assignedChauffeur + '\'' +
                ", dateInterval=" + dateInterval + '\'' +
                ", timeInterval=" + timeInterval + '\'' +
                ", customer=" + customer + '\'' +
                '}';
    }
}