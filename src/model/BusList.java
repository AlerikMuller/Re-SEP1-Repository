package model;

import java.util.ArrayList;

public class BusList {

    private ArrayList<Bus> buses;

    public BusList() {
        this.buses = new ArrayList<>();
    }

    public void addBus(Bus bus) {
        if(!bus.isAvailable())
        {
            throw new IllegalArgumentException("Bus not available");
        }
        this.buses.add(bus);
    }

    public void removeBus(Bus bus) {
        if(bus == null || buses.isEmpty()) {
            throw new IllegalArgumentException("Bus cannot be null.");
        }
        buses.remove(bus);
    }

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

    public Bus getBus(int index) {
        return buses.get(index);
    }

    public Bus getBusByRegNo(String regNo) {
        for (Bus bus : buses) {
            if (bus.getRegNo().equalsIgnoreCase(regNo)) {
                return bus;
            }
        }
        return null;
    }

    public boolean containsRegNo(String regNo) {
        return getBusByRegNo(regNo) != null;
    }

    // Filters by the bus's own isAvailable flag only.
    public BusList getAvailableBuses() {
        BusList available = new BusList();
        for (Bus bus : buses) {
            if (bus.isAvailable()) {
                available.addBus(bus);
            }
        }
        return available;
    }

    // Filters by availability AND bus type (minibus / large bus / special purpose).
    public BusList getAvailableBusesByType(String type) {
        BusList available = new BusList();
        for (Bus bus : buses) {
            if (bus.isAvailable() && bus.getType().equalsIgnoreCase(type)) {
                available.addBus(bus);
            }
        }
        return available;
    }

    public int size() {
        return buses.size();
    }

    public boolean isEmpty() {
        return buses.isEmpty();
    }

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
