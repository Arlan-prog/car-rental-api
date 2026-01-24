package model;

import exception.InvalidInputException;

public class Customer extends BaseEntity implements Validatable {
    private String phone;

    public Customer(int id, String name, String phone) {
        super(id, name);
        this.phone = phone;
    }

    public Customer(String name, String phone) {
        super(name);
        this.phone = phone;
    }

    @Override
    public String getEntityType() {
        return "CUSTOMER";
    }

    @Override
    public String describe() {
        return displayName() + " | phone=" + phone;
    }

    @Override
    public void validate() throws InvalidInputException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Customer name must not be empty.");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new InvalidInputException("Phone must not be empty.");
        }
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
