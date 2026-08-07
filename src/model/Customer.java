package model;

//Stores the basic customer information used throughout trip planning.
public class Customer {
    private String name;
    private String phone;

    //Creates a customer after validating required name and phone.
    public Customer(String name, String phone) {
        setName(name);
        setPhone(phone);
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty.");
        }
        this.name = name.trim();
    }

    public String getName() {
        return name;
    }

    //Ensures customer phone numbers contain digits and are not empty.
    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty.");
        }
        if (!phone.trim().matches("\\d+")) {
            throw new IllegalArgumentException("Phone number must only contain digits.");
        }
        this.phone = phone.trim();
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}