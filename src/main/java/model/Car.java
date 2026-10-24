package model;

import exception.InvalidInputException;

public class Car extends BaseEntity implements PricedItem, Validatable {
    private double dailyPrice;
    private boolean isAvailable;

    public Car(int id, String name, double dailyPrice, boolean isAvailable) {
        super(id, name);
        this.dailyPrice = dailyPrice;
        this.isAvailable = isAvailable;
    }

    public Car(String name, double dailyPrice) {
        super(name);
        this.dailyPrice = dailyPrice;
        this.isAvailable = true;
    }

    @Override
    public String getEntityType() {
        return "CAR";
    }

    @Override
    public String describe() {
        return displayName() + " | dailyPrice=" + dailyPrice + " | available=" + isAvailable;
    }

    @Override
    public double getDailyPrice() {
        return dailyPrice;
    }

    @Override
    public void validate() throws InvalidInputException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Car name must not be empty.");
        }
        if (!Double.isFinite(dailyPrice) || dailyPrice <= 0) {
            throw new InvalidInputException("dailyPrice must be > 0.");
        }
    }

    public void setDailyPrice(double dailyPrice) { this.dailyPrice = dailyPrice; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}
