package model;

import java.time.LocalDate;

public class Rental {
    private int id;
    private Customer customer;
    private Car car;
    private LocalDate startDate;
    private int days;
    private String status; // ACTIVE / FINISHED

    public Rental(int id, Customer customer, Car car, LocalDate startDate, int days, String status) {
        this.id = id;
        this.customer = customer;
        this.car = car;
        this.startDate = startDate;
        this.days = days;
        this.status = status;
    }

    public Rental(Customer customer, Car car, LocalDate startDate, int days) {
        this.customer = customer;
        this.car = car;
        this.startDate = startDate;
        this.days = days;
        this.status = "ACTIVE";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public Car getCar() { return car; }

    public LocalDate getStartDate() { return startDate; }
    public int getDays() { return days; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String describe() {
        return "Rental #" + id +
                " | customer=" + customer.getName() +
                " | car=" + car.getName() +
                " | days=" + days +
                " | status=" + status;
    }
}
