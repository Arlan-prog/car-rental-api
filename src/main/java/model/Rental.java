package model;

import java.time.LocalDate;

public class Rental {
    private int id;
    private final Customer customer;
    private final Car car;
    private final LocalDate startDate;
    private final int days;
    private String status;

    public Rental(int id, Customer customer, Car car, LocalDate startDate, int days, String status) {
        if (id < 0) {
            throw new IllegalArgumentException("Rental id cannot be negative.");
        }
        validate(customer, car, startDate, days);
        if (!"ACTIVE".equals(status) && !"FINISHED".equals(status)) {
            throw new IllegalArgumentException("Rental status must be ACTIVE or FINISHED.");
        }
        this.id = id;
        this.customer = customer;
        this.car = car;
        this.startDate = startDate;
        this.days = days;
        this.status = status;
    }

    public Rental(Customer customer, Car car, LocalDate startDate, int days) {
        this(0, customer, car, startDate, days, "ACTIVE");
    }

    private static void validate(Customer customer, Car car, LocalDate startDate, int days) {
        if (customer == null || car == null || startDate == null || days <= 0) {
            throw new IllegalArgumentException("Customer, car, start date, and positive days are required.");
        }
    }

    public int getId() { return id; }
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Rental id must be positive.");
        }
        this.id = id;
    }

    public Customer getCustomer() { return customer; }
    public Car getCar() { return car; }
    public LocalDate getStartDate() { return startDate; }
    public int getDays() { return days; }
    public String getStatus() { return status; }

    public void setStatus(String status) {
        if (!"ACTIVE".equals(status) && !"FINISHED".equals(status)) {
            throw new IllegalArgumentException("Rental status must be ACTIVE or FINISHED.");
        }
        this.status = status;
    }

    public String describe() {
        return "Rental #" + id
                + " | customer=" + customer.getName()
                + " | car=" + car.getName()
                + " | days=" + days
                + " | status=" + status;
    }
}
