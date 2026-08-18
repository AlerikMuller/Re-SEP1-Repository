package model;

import java.util.ArrayList;

/**
 * A container class that stores and manages a collection of {@link Trip} objects.
 * <p>
 * The {@code TripList} wraps an {@link ArrayList} of trips and provides operations for
 * adding, removing, searching and filtering trips, as well as for assigning buses and
 * chauffeurs and for detecting overlapping assignments. It supports the double-booking
 * checks that prevent the same bus or chauffeur from being assigned to two overlapping
 * trips.
 *
 * @author Ghiyath
 * @version 1.0
 */
public class TripList {

    private ArrayList<Trip> trips;

    /**
     * Creates an empty {@code TripList}.
     */
    public TripList() {
        this.trips = new ArrayList<>();
    }

    /**
     * Adds a trip to the list, provided that it has not already started and does not
     * overlap with an existing trip.
     *
     * @param trip the trip to add
     * @throws IllegalArgumentException if the trip has already started or overlaps an existing trip
     */
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

    /**
     * Removes the given trip from the list, provided that its status allows removal.
     *
     * @param trip the trip to remove
     * @throws IllegalArgumentException if the trip is {@code null} or the list is empty
     */
    public void removeTrip(Trip trip) {
        if(trip == null || trips.isEmpty()) {
            throw new IllegalArgumentException("Trip cannot be null.");
        }
        else if(trip.canBeRemoved()) {
            trips.remove(trip);
        }
    }

    /**
     * Returns the trip at the given position in the list.
     *
     * @param index the position of the trip in the list
     * @return the trip at the specified index
     */
    public Trip getTrip(int index) {
        return trips.get(index);
    }

    /**
     * Returns a new list containing only the trips that have the given status.
     *
     * @param status the status to filter by (for example "Ended")
     * @return a {@code TripList} of trips with the given status
     */
    public TripList getTripsByStatus(String status) {
        TripList result = new TripList();
        for (Trip trip : trips) {
            if (trip.getStatus().equalsIgnoreCase(status)) {
                result.addTrip(trip);
            }
        }
        return result;
    }

    /**
     * Assigns the bus of the given trip to matching trips in the list that do not yet
     * have a chauffeur assigned and are not started.
     *
     * @param tripToAssign the trip whose bus and matching details are used for the assignment
     */
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

    /**
     * Assigns the chauffeur of the given trip to matching trips in the list.
     *
     * @param tripToAssign the trip whose chauffeur and matching details are used for the assignment
     * @throws IllegalArgumentException if a matching trip already has a chauffeur assigned and is not "Not Started"
     */
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

    public void changeUsualTripStatus(Trip tripToUpdate)
    {
        for (Trip trip : trips)
        {
            if (trip.getOrigin().equals(tripToUpdate.getOrigin())
                    || trip.getDestination().equals(tripToUpdate.getDestination())
                    || trip.getAssignedBus() == tripToUpdate.getAssignedBus()
                    || trip.getAssignedChauffeur() == tripToUpdate.getAssignedChauffeur()
                    || trip.getDateInterval() == tripToUpdate.getDateInterval()
                    || trip.getTimeInterval() == tripToUpdate.getTimeInterval())
            {
                trip.setStatus(tripToUpdate.getStatus());
            }
        }
    }

    public void changeCustomerTripStatus(Trip tripToUpdate)
    {
        for (Trip trip : trips)
        {
            if (trip.getOrigin().equals(tripToUpdate.getOrigin())
                    || trip.getDestination().equals(tripToUpdate.getDestination())
                    || trip.getAssignedBus() == tripToUpdate.getAssignedBus()
                    || trip.getAssignedChauffeur() == tripToUpdate.getAssignedChauffeur()
                    || trip.getDateInterval() == tripToUpdate.getDateInterval()
                    || trip.getTimeInterval() == tripToUpdate.getTimeInterval()
                    || trip.getCustomer() == tripToUpdate.getCustomer())
            {
                trip.setStatus(tripToUpdate.getStatus());
            }
        }
    }

    /**
     * Checks whether the given bus or chauffeur is already assigned to another trip whose
     * date and time interval overlaps the given intervals. This implements the
     * double-booking prevention rule.
     *
     * @param bus          the bus to check, or {@code null} to ignore the bus
     * @param chauffeur    the chauffeur to check, or {@code null} to ignore the chauffeur
     * @param dateInterval the date interval to check for overlaps
     * @param timeInterval the time interval to check for overlaps
     * @return {@code true} if an overlapping assignment exists, otherwise {@code false}
     */
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

    /**
     * Returns a new list of all trips that the given bus is currently assigned to.
     * Used when checking for double-booking of a bus.
     *
     * @param bus the bus to find trips for
     * @return a {@code TripList} of trips assigned to the given bus
     */
    public TripList getTripsForBus(Bus bus) {
        TripList result = new TripList();

        // Let n represent the number of trips in the original list.
        // This loop can run n times because every trip may need checking.
        for (Trip trip : trips) {

            // Comparing the bus with the assigned bus takes constant time, O(1).
            if (bus.equals(trip.getAssignedBus())) {

                // addTrip() loops through the current result list before adding.
                // In the worst case, every original trip matches this bus.
                // The result list therefore grows from 0 up to n - 1 trips.
                result.addTrip(trip);
            }
        }

        // Worst case:
        // T(n) = n + (0 + 1 + 2 + ... + (n - 1))
        // T(n) = n + n(n - 1) / 2
        // Ignoring constants and lower-order terms gives T(n) = O(n^2).
        //
        // Best case: O(n), when no trips match the given bus.
        // Worst case: O(n^2), when every trip matches the given bus.
        return result;
    }

    /**
     * Returns a new list of all trips that the given chauffeur is currently assigned to.
     * Used when checking for double-booking of a chauffeur.
     *
     * @param chauffeur the chauffeur to find trips for
     * @return a {@code TripList} of trips assigned to the given chauffeur
     */
    public TripList getTripsForChauffeur(Chauffeur chauffeur) {
        TripList result = new TripList();
        for (Trip trip : trips) {
            if (chauffeur.equals(trip.getAssignedChauffeur())) {
                result.addTrip(trip);
            }
        }
        return result;
    }

    /**
     * Returns the number of trips in the list.
     *
     * @return the number of trips
     */
    public int size() {
        return trips.size();
    }

    /**
     * Checks whether the list contains no trips.
     *
     * @return {@code true} if the list is empty, otherwise {@code false}
     */
    public boolean isEmpty() {
        return trips.isEmpty();
    }

    /**
     * Returns a string representation of the list, containing the string
     * representation of every trip it holds.
     *
     * @return a string describing all trips in the list
     */
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