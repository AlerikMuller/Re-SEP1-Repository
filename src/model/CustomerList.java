package model;

import java.util.ArrayList;


public class CustomerList {

    private ArrayList<Customer> customers;

    public CustomerList() {
        this.customers = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        customers.add(customer);
    }

    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }

    public Customer getCustomer(int index) {
        return customers.get(index);
    }

    public Customer getCustomerByPhone(String phone) {
        for (Customer customer : customers) {
            if (customer.getPhone().equalsIgnoreCase(phone)) {
                return customer;
            }
        }
        return null;
    }

    public boolean containsPhone(String phone) {
        return getCustomerByPhone(phone) != null;
    }

    public int size() {
        return customers.size();
    }

    public boolean isEmpty() {
        return customers.isEmpty();
    }

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
