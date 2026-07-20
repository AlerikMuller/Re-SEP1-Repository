package model;

public class Trip {
    private String origin;
    private String destination;
    private String status;
    private Bus assignedBus;
    private Chauffeur assignedChauffeur;
    private DateInterval dateInterval;
    private TimeInterval timeInterval;
    private Customer customer;

    public Trip(String origin, String destination, String status, Bus assignedBus,
                Chauffeur assignedChauffeur, DateInterval dateInterval, TimeInterval timeInterval) {
        setOrigin(origin);
        setDestination(destination);
        setStatus(status);
        assignBus(assignedBus);
        assignChauffeur(assignedChauffeur);
        setDateInterval(dateInterval);
        setTimeInterval(timeInterval);
    }

    public Trip(String origin, String destination, String status, Bus assignedBus,
                Chauffeur assignedChauffeur, DateInterval dateInterval,
                TimeInterval timeInterval, Customer customer) {
        this(origin, destination, status, assignedBus, assignedChauffeur, dateInterval, timeInterval);
        setCustomer(customer);
    }

    public void setOrigin(String originAddress) {
        if (originAddress == null || originAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Origin address cannot be empty.");
        }
        this.origin = originAddress.trim();
    }

    public String getOrigin() {
        return origin;
    }

    public void setDestination(String destination) {
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination address cannot be empty.");
        }
        this.destination = destination.trim();
    }

    public String getDestination() {
        return destination;
    }

    public void setStatus(String status) {
        this.status = normalizeStatus(status);
    }

    public String getStatus() {
        return status;
    }

    private String normalizeStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Trip status cannot be empty.");
        }

        String normalized = status.trim().toLowerCase();

        switch (normalized) {
            case "not started":
            case "notstarted":
            case "waiting":
                return "Not Started";
            case "started":
                return "Started";
            case "cancelled":
            case "canceled":
                return "Cancelled";
            case "ended":
                return "Ended";
            default:
                throw new IllegalArgumentException("Status must be Not Started, Started, Cancelled, or Ended.");
        }
    }

    public void assignBus(Bus bus) {
        if (bus == null) {
            throw new IllegalArgumentException("Assigned bus cannot be empty.");
        }
        this.assignedBus = bus;
    }

    public Bus getAssignedBus() {
        return assignedBus;
    }

    public void assignChauffeur(Chauffeur chauffeur) {
        if (chauffeur == null) {
            throw new IllegalArgumentException("Assigned chauffeur cannot be empty.");
        }
        this.assignedChauffeur = chauffeur;
    }

    public Chauffeur getAssignedChauffeur() {
        return assignedChauffeur;
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

    public String getDateIntervalString() {
        return dateInterval.toString();
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

    public String getTimeIntervalString() {
        return timeInterval.toString();
    }

    public void setCustomer(String name, String phone) {
        this.customer = new Customer(name, phone);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be empty.");
        }
        this.customer = customer;
    }

    public boolean canBeEdited() {
        return status.equals("Not Started") || status.equals("Cancelled");
    }

    public boolean canBeRemoved() {
        return status.equals("Not Started") || status.equals("Cancelled");
    }

    public boolean hasStarted() {
        return status.equals("Started") || status.equals("Ended");
    }

    public boolean overlaps(Trip other) {
        if (other == null) {
            throw new IllegalArgumentException("Other trip cannot be empty.");
        }

        return this.dateInterval.overlaps(other.dateInterval) &&
                this.timeInterval.overlaps(other.timeInterval);
    }

    @Override
    public String toString() {
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