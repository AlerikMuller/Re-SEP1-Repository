package model;

/**
 * Represents a trip registered in the Horsens Tours planning system.
 * The class stores route information, status, date and time intervals,
 * the assigned bus and chauffeur, and an optional associated customer.
 * It also provides operations for assignment, editing, removal, and
 * detecting overlaps with other registered trips.
 *
 * @author Alerik Muller
 * @version 1.0
 */
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

    /**
     * Creates an empty trip without assigning any initial information.
     */
    public Trip()
    {
    }

    /**
     * Creates a trip with route, status, resources, and date-time intervals.
     *
     * @param origin the trip origin
     * @param destination the trip destination
     * @param status the trip status
     * @param assignedBus the bus assigned to the trip
     * @param assignedChauffeur the chauffeur assigned to the trip
     * @param dateInterval the trip date interval
     * @param timeInterval the trip time interval
     */
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

    /**
     * Creates a trip containing route, status, and date-time information
     * without initially assigning a bus or chauffeur.
     *
     * @param origin the trip origin
     * @param destination the trip destination
     * @param status the trip status
     * @param dateInterval the trip date interval
     * @param timeInterval the trip time interval
     */
    public Trip(String origin, String destination, String status, DateInterval dateInterval, TimeInterval timeInterval)
    {
        setOrigin(origin);
        setDestination(destination);
        setStatus(status);
        setDateInterval(dateInterval);
        setTimeInterval(timeInterval);
    }

    /**
     * Creates a complete trip including assigned resources and an optional customer.
     *
     * @param origin the trip origin
     * @param destination the trip destination
     * @param status the trip status
     * @param assignedBus the bus assigned to the trip
     * @param assignedChauffeur the chauffeur assigned to the trip
     * @param dateInterval the trip date interval
     * @param timeInterval the trip time interval
     * @param customer the customer associated with the trip
     */
    public Trip(String origin, String destination, String status, Bus assignedBus, Chauffeur assignedChauffeur, DateInterval dateInterval, TimeInterval timeInterval, Customer customer)
    {
        this(origin, destination, status, assignedBus, assignedChauffeur, dateInterval, timeInterval);
        setCustomer(customer);
    }

    /**
     * Sets the origin address of the trip.
     *
     * @param originAddress the origin address to store
     * @throws IllegalArgumentException if the origin is null or empty
     */
    public void setOrigin(String originAddress)
    {
        if (originAddress == null || originAddress.trim().isEmpty())
        {
            throw new IllegalArgumentException("Origin address cannot be empty.");
        }

        this.origin = originAddress.trim();
    }

    /**
     * Returns the origin address of the trip.
     *
     * @return the trip origin
     */
    public String getOrigin()
    {
        return origin;
    }

    /**
     * Sets the destination address of the trip.
     *
     * @param destination the destination address to store
     * @throws IllegalArgumentException if the destination is null or empty
     */
    public void setDestination(String destination)
    {
        if (destination == null || destination.trim().isEmpty())
        {
            throw new IllegalArgumentException("Destination address cannot be empty.");
        }

        this.destination = destination.trim();
    }

    /**
     * Returns the destination address of the trip.
     *
     * @return the trip destination
     */
    public String getDestination()
    {
        return destination;
    }

    /**
     * Sets the trip status to one of the supported workflow states.
     *
     * @param status the status to store
     * @throws IllegalArgumentException if the status is null, empty, or unsupported
     */
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

    /**
     * Returns the current status of the trip.
     *
     * @return the trip status
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * Assigns a bus to the trip.
     *
     * @param bus the bus to assign
     * @throws IllegalArgumentException if the bus is {@code null}
     */
    public void assignBus(Bus bus)
    {
        if (bus == null)
        {
            throw new IllegalArgumentException("Assigned Bus cannot be null.");
        }

        this.assignedBus = bus;
    }

    /**
     * Checks whether a bus is currently assigned to the trip.
     *
     * @return {@code true} if a bus is assigned, otherwise {@code false}
     */
    public boolean isBusAssigned()
    {
        return assignedBus != null;
    }

    /**
     * Returns the bus currently assigned to the trip.
     *
     * @return the assigned bus
     */
    public Bus getAssignedBus()
    {
        return assignedBus;
    }

    /**
     * Assigns a chauffeur to the trip.
     *
     * @param chauffeur the chauffeur to assign
     * @throws IllegalArgumentException if the chauffeur is {@code null}
     */
    public void assignChauffeur(Chauffeur chauffeur)
    {
        if (chauffeur == null)
        {
            throw new IllegalArgumentException("Assigned chauffeur cannot be empty.");
        }

        this.assignedChauffeur = chauffeur;
    }

    /**
     * Returns the chauffeur currently assigned to the trip.
     *
     * @return the assigned chauffeur
     */
    public Chauffeur getAssignedChauffeur()
    {
        return assignedChauffeur;
    }

    /**
     * Sets the date interval of the trip.
     *
     * @param dateInterval the date interval to assign
     * @throws IllegalArgumentException if the interval is {@code null}
     */
    public void setDateInterval(DateInterval dateInterval)
    {
        if (dateInterval == null)
        {
            throw new IllegalArgumentException("Date interval cannot be empty.");
        }

        this.dateInterval = dateInterval;
    }

    /**
     * Returns the date interval of the trip.
     *
     * @return the trip date interval
     */
    public DateInterval getDateInterval()
    {
        return dateInterval;
    }

    /**
     * Returns the date interval of the trip.
     *
     * @return the trip date interval
     */
    public String getDateIntervalString()
    {
        return dateInterval == null ? "" : dateInterval.toString();
    }

    /**
     * Sets the time interval of the trip.
     *
     * @param timeInterval the time interval to assign
     * @throws IllegalArgumentException if the interval is {@code null}
     */
    public void setTimeInterval(TimeInterval timeInterval)
    {
        if (timeInterval == null)
        {
            throw new IllegalArgumentException("Time interval cannot be empty.");
        }

        this.timeInterval = timeInterval;
    }

    /**
     * Returns the time interval of the trip.
     *
     * @return the trip time interval
     */
    public TimeInterval getTimeInterval()
    {
        return timeInterval;
    }

    /**
     * Returns the time interval as a displayable string.
     *
     * @return the time interval string, or an empty string if none exists
     */
    public String getTimeIntervalString()
    {
        return timeInterval == null ? "" : timeInterval.toString();
    }

    /**
     * Creates and assigns a customer using the supplied name and phone number.
     *
     * @param name the customer's name
     * @param phone the customer's phone number
     */
    public void setCustomer(String name, String phone)
    {
        this.customer = new Customer(name, phone);
    }

    /**
     * Returns the customer associated with the trip.
     *
     * @return the associated customer, or {@code null} if none is assigned
     */
    public Customer getCustomer()
    {
        return customer;
    }

    /**
     * Assigns an existing customer to the trip.
     *
     * @param customer the customer to associate, or {@code null} to remove the association
     */
    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    /**
     * Removes the currently associated customer from the trip.
     */
    public void removeCustomer()
    {
        this.customer = null;
    }

    /**
     * Checks whether the trip may be edited according to its current status.
     *
     * @return {@code true} for Not Started, Cancelled, or Ended trips
     */
    public boolean canBeEdited()
    {
        return status.equals("Not Started") || status.equals("Cancelled") || status.equals("Ended");
    }

    /**
     * Checks whether the trip may be removed according to its current status.
     *
     * @return {@code true} for Not Started, Cancelled, or Ended trips
     */
    public boolean canBeRemoved()
    {
        return status.equals("Not Started") || status.equals("Cancelled") || status.equals("Ended");
    }

    /**
     * Checks whether the trip is considered to have started or ended.
     *
     * @return {@code true} if the status is Started or Ended
     */
    public boolean hasStarted()
    {
        return status.equals("Started") || status.equals("Ended");
    }

    /**
     * Checks whether this trip overlaps another trip in both date and time.
     *
     * @param other the trip to compare with
     * @return {@code true} if both the date and time intervals overlap
     * @throws IllegalArgumentException if the other trip is {@code null}
     */
    public boolean overlaps(Trip other)
    {
        if (other == null)
        {
            throw new IllegalArgumentException("Other trip cannot be empty.");
        }

        return this.dateInterval.overlaps(other.dateInterval) && this.timeInterval.overlaps(other.timeInterval);
    }

    /**
     * Returns a string containing the stored trip information and assignments.
     *
     * @return a string representation of the trip
     */
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