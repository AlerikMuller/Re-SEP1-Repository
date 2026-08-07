package model;

import java.util.ArrayList;

/**
 * A container class that stores and manages a collection of {@link Customer} objects.
 * <p>
 * The {@code CustomerList} wraps an {@link ArrayList} of customers and provides
 * operations for adding, removing, searching and inspecting the customers held by the
 * trip-planning company. Customers are identified by their phone number.
 *
 * @author Ghiyath
 * @version 1.0
 */
public class CustomerList {

    private ArrayList<Customer> customers;

    /**
     * Creates an empty {@code CustomerList}.
     */
    public CustomerList() {
        this.customers = new ArrayList<>();
    }

    /**
     * Adds a customer to the list.
     *
     * @param customer the customer to add
     * @throws IllegalArgumentException if the customer is {@code null}
     */
    public void addCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        customers.add(customer);
    }

    /**
     * Removes the given customer from the list.
     *
     * @param customer the customer to remove
     */
    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }

    /**
     * Returns the customer at the given position in the list.
     *
     * @param index the position of the customer in the list
     * @return the customer at the specified index
     */
    public Customer getCustomer(int index) {
        return customers.get(index);
    }

    /**
     * Searches for a customer by phone number, ignoring case.
     *
     * @param phone the phone number to search for
     * @return the matching customer, or {@code null} if no customer has that phone number
     */
    public Customer getCustomerByPhone(String phone) {
        for (Customer customer : customers) {
            if (customer.getPhone().equalsIgnoreCase(phone)) {
                return customer;
            }
        }
        return null;
    }

    /**
     * Checks whether the list contains a customer with the given phone number.
     *
     * @param phone the phone number to look for
     * @return {@code true} if a customer with that phone number exists, otherwise {@code false}
     */
    public boolean containsPhone(String phone) {
        return getCustomerByPhone(phone) != null;
    }

    /**
     * Returns the number of customers in the list.
     *
     * @return the number of customers
     */
    public int size() {
        return customers.size();
    }

    /**
     * Checks whether the list contains no customers.
     *
     * @return {@code true} if the list is empty, otherwise {@code false}
     */
    public boolean isEmpty() {
        return customers.isEmpty();
    }

    /**
     * Returns a string representation of the list, containing the string
     * representation of every customer it holds.
     *
     * @return a string describing all customers in the list
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CustomerList{\n");
        for (Customer customer : customers) {
            sb.append("  ").append(customer.toString()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}