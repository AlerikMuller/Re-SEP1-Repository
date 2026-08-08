package model;

/**
 * Represents a bus used by Horsens Tours and stores the information
 * needed for registration, management, and assignment to trips.
 * The class keeps track of registration number, bus type, rental
 * price, seat capacity, and whether the bus is currently available.
 *
 * @author Alerik Muller
 * @version 1.0
 */
public class Bus {
    private String regNo;
    private String type;
    private float rentPricePerDay;
    private int seatCapacity;
    private boolean isAvailable;

    /**
     * Creates a bus with the given registration information.
     *
     * @param regNo the registration number of the bus
     * @param type the type of the bus
     * @param rentPricePerDay the rental price per day
     * @param seatCapacity the number of passenger seats
     * @param availability whether the bus is currently available
     */
    public Bus(String regNo, String type, float rentPricePerDay, int seatCapacity, boolean availability) {
        setBus(regNo, type, rentPricePerDay, seatCapacity, availability);
    }

    /**
     * Sets the registration number of the bus.
     *
     * @param regNo the registration number to store
     * @throws IllegalArgumentException if the registration number is null or empty
     */
    public void setRegNo(String regNo) {
        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be empty.");
        }
        this.regNo = regNo.trim();
    }

    /**
     * Returns the registration number of the bus.
     *
     * @return the bus registration number
     */
    public String getRegNo() {
        return regNo;
    }

    /**
     * Sets the bus type when it matches one of the supported types.
     *
     * @param type the bus type to store
     * @throws IllegalArgumentException if the type is null or empty
     */
    public void setType(String type)
    {
        if (type == null || type.trim().isEmpty())
        {
            throw new IllegalArgumentException("Bus type cannot be empty.");
        }
        if (type.equalsIgnoreCase("Mini bus") || type.equalsIgnoreCase(
            "Large bus") || type.equalsIgnoreCase("Special purpose bus"))
        {
            this.type = type.trim();
        }
    }

    /**
     * Returns the type of the bus.
     *
     * @return the stored bus type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the rental price charged per day for the bus.
     *
     * @param rentPricePerDay the rental price per day
     * @throws IllegalArgumentException if the rental price is negative
     */
    public void setRentPricePerDay(float rentPricePerDay) {
        if (rentPricePerDay < 0) {
            throw new IllegalArgumentException("Rent price per day cannot be negative.");
        }
        this.rentPricePerDay = rentPricePerDay;
    }

    /**
     * Returns the rental price charged per day for the bus.
     *
     * @return the rental price per day
     */
    public float getRentPricePerDay() {
        return rentPricePerDay;
    }

    /**
     * Updates all stored information for the bus using the individual setters.
     *
     * @param regNo the registration number of the bus
     * @param type the type of the bus
     * @param rentPricePerDay the rental price per day
     * @param seatCapacity the number of passenger seats
     * @param availability whether the bus is currently available
     */
    public void setBus(String regNo, String type, float rentPricePerDay, int seatCapacity, boolean availability) {
        setRegNo(regNo);
        setType(type);
        setRentPricePerDay(rentPricePerDay);
        setSeatCapacity(seatCapacity);
        setAvailability(availability);
    }

    /**
     * Sets whether the bus is currently available for use.
     *
     * @param availability the new availability value
     */
    public void setAvailability(boolean availability) {
        this.isAvailable = availability;
    }

    /**
     * Returns whether the bus is currently available.
     *
     * @return {@code true} if the bus is available, otherwise {@code false}
     */
    public boolean getAvailability() {
        return isAvailable;
    }

    /**
     * Sets the passenger seat capacity of the bus.
     *
     * @param seatCapacity the number of passenger seats
     * @throws IllegalArgumentException if the seat capacity is negative
     */
    public void setSeatCapacity(int seatCapacity) {
        if (seatCapacity < 0) {
            throw new IllegalArgumentException("Seat capacity cannot be negative.");
        }
        this.seatCapacity = seatCapacity;
    }

    /**
     * Returns the passenger seat capacity of the bus.
     *
     * @return the number of passenger seats
     */
    public int getSeatCapacity() {
        return seatCapacity;
    }

    /**
     * Checks whether the bus is currently available.
     *
     * @return {@code true} if the bus is available, otherwise {@code false}
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Returns a string containing all stored information about the bus.
     *
     * @return a string representation of the bus
     */
    @Override
    public String toString() {
        return "Bus{" +
                "regNo='" + regNo + '\'' +
                ", type='" + type + '\'' +
                ", rentPricePerDay=" + rentPricePerDay +
                ", seatCapacity=" + seatCapacity +
                ", isAvailable=" + isAvailable +
                '}';
    }
}