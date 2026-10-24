package service;

import exception.DatabaseOperationException;
import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import model.Car;
import model.Customer;
import model.Rental;
import repository.CarRepository;
import repository.CustomerRepository;
import repository.RentalRepository;

import java.time.LocalDate;
import java.util.List;

public class RentalService {

    private final RentalRepository rentalRepo = new RentalRepository();
    private final CustomerRepository customerRepo = new CustomerRepository();
    private final CarRepository carRepo = new CarRepository();

    public Rental create(int customerId, int carId, LocalDate startDate, int days)
            throws InvalidInputException, ResourceNotFoundException, DatabaseOperationException {
        if (customerId <= 0 || carId <= 0 || startDate == null || days <= 0) {
            throw new InvalidInputException("Customer, car, start date, and a positive number of days are required.");
        }

        Customer customer = customerRepo.getById(customerId);
        if (customer == null) {
            throw new ResourceNotFoundException("Customer not found.");
        }

        Car car = carRepo.getById(carId);
        if (car == null) {
            throw new ResourceNotFoundException("Car not found.");
        }
        if (!car.isAvailable()) {
            throw new InvalidInputException("Car is not available.");
        }
        if (rentalRepo.hasActiveRentalForCustomer(customerId)) {
            throw new InvalidInputException("Customer already has an active rental.");
        }

        // Reserve the car and insert the rental in one database transaction.
        return rentalRepo.create(new Rental(customer, car, startDate, days));
    }

    public List<Rental> getAll() throws DatabaseOperationException {
        return rentalRepo.getAll(customerRepo, carRepo);
    }

    public void closeRental(int rentalId)
            throws ResourceNotFoundException, DatabaseOperationException {
        if (rentalId <= 0 || !rentalRepo.closeRental(rentalId)) {
            throw new ResourceNotFoundException("Active rental not found.");
        }
    }
}
