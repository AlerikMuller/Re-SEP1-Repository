package model;

import java.util.ArrayList;

/**
 * A container class that stores and manages a collection of {@link Bus} objects.
 * <p>
 * The {@code BusList} wraps an {@link ArrayList} of buses and provides operations
 * for adding, removing, searching, filtering and inspecting the buses held by the
 * trip-planning company. It is used by the model manager and the view controllers
 * whenever a group of buses needs to be handled as a single unit.
 *
 * @author Ghiyath
 * @version 1.0
 */
public class BusList {

    private ArrayList<Bus> buses;

    /**
     * Creates an empty {@code BusList}.
     */
    public BusList() {
        this.buses = new ArrayList<>();
    }

    /**
     * Adds a bus to the list.
     *
     * @param bus the bus to add
     * @throws IllegalArgumentException if the bus is not available
     */
    public void addBus(Bus bus) {
        if(!bus.isAvailable())
        {
            throw new IllegalArgumentException("Bus not available");
        }
        this.buses.add(bus);
    }

    /**
     * Removes the given bus from the list.
     *
     * @param bus the bus to remove
     * @throws IllegalArgumentException if the bus is {@code null} or the list is empty
     */
    public void removeBus(Bus bus) {
        if(bus == null || buses.isEmpty()) {
            throw new IllegalArgumentException("Bus cannot be null.");
        }
        buses.remove(bus);
    }

    /**
     * Returns the underlying list of all buses.
     *
     * @return an {@link ArrayList} containing all buses in this list
     */
    public ArrayList<Bus> getAllBuses()
    {
        return this.buses;
    }

    /**
     * Updates the availability of the bus whose registration number matches the
     * given bus.
     *
     * @param availability the new availability value to set
     * @param busToUpdate  the bus whose registration number identifies the bus to update
     */
    public void updateBusAvailability(boolean availability, Bus busToUpdate)
    {
        for(Bus bus : buses)
        {
            if(bus.getRegNo().equals(busToUpdate.getRegNo()))
            {
                bus.setAvailability(availability);
            }
        }
    }

    /**
     * Returns the bus at the given position in the list.
     *
     * @param index the position of the bus in the list
     * @return the bus at the specified index
     */
    public Bus getBus(int index) {
        return buses.get(index);
    }

    /**
     * Searches for a bus by its registration number, ignoring case.
     *
     * @param regNo the registration number to search for
     * @return the matching bus, or {@code null} if no bus has that registration number
     */
    public Bus getBusByRegNo(String regNo) {
        for (Bus bus : buses) {
            if (bus.getRegNo().equalsIgnoreCase(regNo)) {
                return bus;
            }
        }
        return null;
    }

    /**
     * Checks whether the list contains a bus with the given registration number.
     *
     * @param regNo the registration number to look for
     * @return {@code true} if a bus with that registration number exists, otherwise {@code false}
     */
    public boolean containsRegNo(String regNo) {
        return getBusByRegNo(regNo) != null;
    }

    /**
     * Returns a new list containing only the buses that are currently available.
     *
     * @return a {@code BusList} of available buses
     */
    public BusList getAvailableBuses() {
        BusList available = new BusList();
        for (Bus bus : buses) {
            if (bus.isAvailable()) {
                available.addBus(bus);
            }
        }
        return available;
    }

    /**
     * Returns a new list containing only the buses that are available and match the
     * given bus type (for example minibus, large bus or special-purpose bus).
     *
     * @param type the bus type to filter by
     * @return a {@code BusList} of available buses of the given type
     */
    public BusList getAvailableBusesByType(String type) {
        BusList available = new BusList();
        for (Bus bus : buses) {
            if (bus.isAvailable() && bus.getType().equalsIgnoreCase(type)) {
                available.addBus(bus);
            }
        }
        return available;
    }

    /**
     * Returns the number of buses in the list.
     *
     * @return the number of buses
     */
    public int size() {
        return buses.size();
    }

    /**
     * Checks whether the list contains no buses.
     *
     * @return {@code true} if the list is empty, otherwise {@code false}
     */
    public boolean isEmpty() {
        return buses.isEmpty();
    }

    /**
     * Returns a string representation of the list, containing the string
     * representation of every bus it holds.
     *
     * @return a string describing all buses in the list
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BusList{\n");
        for (Bus bus : buses) {
            sb.append("  ").append(bus.toString()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}