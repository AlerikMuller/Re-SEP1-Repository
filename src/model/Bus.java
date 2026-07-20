package model;

public class Bus {
    private String regNo;
    private String type;
    private float rentPricePerDay;
    private int seatCapacity;
    private boolean isAvailable;

    public Bus(String regNo, String type, float rentPricePerDay, int seatCapacity, boolean availability) {
        setBus(regNo, type, rentPricePerDay, seatCapacity, availability);
    }

    public void setRegNo(String regNo) {
        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be empty.");
        }
        this.regNo = regNo.trim();
    }

    public String getRegNo() {
        return regNo;
    }

    public void setType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Bus type cannot be empty.");
        }
        this.type = type.trim();
    }

    public String getType() {
        return type;
    }

    public void setRentPricePerDay(float rentPricePerDay) {
        if (rentPricePerDay < 0) {
            throw new IllegalArgumentException("Rent price per day cannot be negative.");
        }
        this.rentPricePerDay = rentPricePerDay;
    }

    public float getRentPricePerDay() {
        return rentPricePerDay;
    }

    public void setBus(String regNo, String type, float rentPricePerDay, int seatCapacity, boolean availability) {
        setRegNo(regNo);
        setType(type);
        setRentPricePerDay(rentPricePerDay);
        setSeatCapacity(seatCapacity);
        setAvailability(availability);
    }

    public void setAvailability(boolean availability) {
        this.isAvailable = availability;
    }

    public boolean getAvailability() {
        return isAvailable;
    }

    public void setSeatCapacity(int seatCapacity) {
        if (seatCapacity < 0) {
            throw new IllegalArgumentException("Seat capacity cannot be negative.");
        }
        this.seatCapacity = seatCapacity;
    }

    public int getSeatCapacity() {
        return seatCapacity;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

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