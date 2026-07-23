package model;

import java.util.ArrayList;

public class TripList {

    private ArrayList<Trip> trips;

    public TripList() {
        this.trips = new ArrayList<>();
    }

    public void addTrip(Trip trip) {
        for(int i=0; i<trips.size(); i++)
        {
            if (trip.hasStarted() || trips.get(i).overlaps(trip))
            {
                throw new IllegalArgumentException("Trip already exists.");
            }
        }
                trips.add(trip);
        }

    public void removeTrip(Trip trip) {
        if(trip == null || trips.isEmpty()) {
            throw new IllegalArgumentException("Trip cannot be null.");
        }
        else if(trip.canBeRemoved()) {
            trips.remove(trip);
        }
    }

    public Trip getTrip(int index) {
        return trips.get(index);
    }

    public TripList getTripsByStatus(String status) {
        TripList result = new TripList();
        for (Trip trip : trips) {
            if (trip.getStatus().equalsIgnoreCase(status)) {
                result.addTrip(trip);
            }
        }
        return result;
    }

    public void changeUsualTripStatus(Trip tripToUpdate)
    {
        for(Trip trip : trips)
        {
            if (trip.getOrigin().equals(tripToUpdate.getOrigin())
                || trip.getDestination().equals(tripToUpdate.getDestination())
                || trip.getAssignedBus() == tripToUpdate.getAssignedBus()
                || trip.getAssignedChauffeur()
                == tripToUpdate.getAssignedChauffeur()
                || trip.getDateInterval() == tripToUpdate.getDateInterval()
                || trip.getTimeInterval() == tripToUpdate.getTimeInterval())
            {
                trip.setStatus(tripToUpdate.getStatus());
            }
        }
    }
    public void changeCustomerTripStatus(Trip tripToUpdate)
    {
        for(Trip trip : trips) {
            if (trip.getOrigin().equals(tripToUpdate.getOrigin()) || trip.getDestination().equals(tripToUpdate.getDestination()) || trip.getAssignedBus() == tripToUpdate.getAssignedBus() || trip.getAssignedChauffeur() == tripToUpdate.getAssignedChauffeur() || trip.getDateInterval() == tripToUpdate.getDateInterval() || trip.getTimeInterval() == tripToUpdate.getTimeInterval() || trip.getCustomer() == tripToUpdate.getCustomer()) {
                trip.setStatus(tripToUpdate.getStatus());
            }
        }
    }

    public TripList getPastTrips() {
        return getTripsByStatus("Ended");
    }

    public TripList getActiveTrips() {
        TripList result = new TripList();
        for (Trip trip : trips) {
            String status = trip.getStatus();
            if (status.equalsIgnoreCase("Not Started") || status.equalsIgnoreCase("Started")) {
                result.addTrip(trip);
            }
        }
        return result;
    }

    public void assignBusToTrip(Trip tripToAssign)
    {
        for (Trip trip : trips)
        {
            if (trip.getOrigin().equals(tripToAssign.getOrigin())
                || trip.getDestination().equals(tripToAssign.getDestination())
                || trip.getDateInterval() == tripToAssign.getDateInterval()
                || trip.getTimeInterval() == tripToAssign.getTimeInterval())
            {
                if (trip.getAssignedChauffeur() == null && trip.getStatus()
                    .equalsIgnoreCase("Not Started"))
                {
                    trip.assignBus(tripToAssign.getAssignedBus());
                }
            }
        }
    }

    public void assignChauffeurToTrip(Trip tripToAssign)
    {
        for(Trip trip : trips)
        {
            if (trip.getOrigin().equals(tripToAssign.getOrigin()) || trip.getDestination().equals(tripToAssign.getDestination()) || trip.getDateInterval() == tripToAssign.getDateInterval() || trip.getTimeInterval() == tripToAssign.getTimeInterval())
            {
                if(trip.getAssignedChauffeur() != null && !trip.getStatus().equalsIgnoreCase("Not Started"))
                {
                   throw new IllegalArgumentException("Chauffeur already assigned.");
                }
                trip.assignChauffeur(tripToAssign.getAssignedChauffeur());
            }
        }
    }

    public boolean hasOverlappingAssignment(Bus bus, Chauffeur chauffeur,
                                             DateInterval dateInterval, TimeInterval timeInterval) {
        for (Trip trip : trips) {
            boolean sameBus = bus != null && bus.equals(trip.getAssignedBus());
            boolean sameChauffeur = chauffeur != null && chauffeur.equals(trip.getAssignedChauffeur());
            if (sameBus || sameChauffeur) {
                boolean dateOverlaps = trip.getDateInterval().overlaps(dateInterval);
                boolean timeOverlaps = trip.getTimeInterval().overlaps(timeInterval);
                if (dateOverlaps && timeOverlaps) {
                    return true;
                }
            }
        }
        return false;
    }

    // Used to check for double-booking: all trips currently assigned to this bus.
    public TripList getTripsForBus(Bus bus) {
        TripList result = new TripList();
        for (Trip trip : trips) {
            if (bus.equals(trip.getAssignedBus())) {
                result.addTrip(trip);
            }
        }
        return result;
    }

    // Used to check for double-booking: all trips currently assigned to this chauffeur.
    public TripList getTripsForChauffeur(Chauffeur chauffeur) {
        TripList result = new TripList();
        for (Trip trip : trips) {
            if (chauffeur.equals(trip.getAssignedChauffeur())) {
                result.addTrip(trip);
            }
        }
        return result;
    }

    public int size() {
        return trips.size();
    }

    public boolean isEmpty() {
        return trips.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TripList{\n");
        for (Trip trip : trips) {
            sb.append("  ").append(trip.toString()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
