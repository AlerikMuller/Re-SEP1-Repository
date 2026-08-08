package model;

/**
 * Represents a customer registered in the Horsens Tours system.
 * The class stores the customer's name and phone number so the
 * customer can be managed and optionally connected to registered trips.
 *
 * @author Alerik Muller
 * @version 1.0
 */
public class Customer {
    private String name;
    private String phone;

    /**
     * Creates a customer with the given name and phone number.
     *
     * @param name the customer's name
     * @param phone the customer's phone number
     */
    public Customer(String name, String phone) {
        setName(name);
        setPhone(phone);
    }

    /**
     * Sets the customer's name.
     *
     * @param name the name to store
     * @throws IllegalArgumentException if the name is null or empty
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty.");
        }
        this.name = name.trim();
    }

    /**
     * Returns the customer's name.
     *
     * @return the customer's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the customer's phone number and requires digits only.
     *
     * @param phone the phone number to store
     * @throws IllegalArgumentException if the phone is empty or contains non-digits
     */
    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty.");
        }
        if (!phone.trim().matches("\\d+")) {
            throw new IllegalArgumentException("Phone number must only contain digits.");
        }
        this.phone = phone.trim();
    }

    /**
     * Returns the customer's phone number.
     *
     * @return the customer's phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Returns a string containing the stored customer information.
     *
     * @return a string representation of the customer
     */
    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}