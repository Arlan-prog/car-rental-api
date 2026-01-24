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

    // БИЗНЕС-ПРАВИЛО: 1 клиент = 1 активная аренда
    public Rental create(int customerId, int carId, LocalDate startDate, int days)
            throws InvalidInputException, ResourceNotFoundException, DatabaseOperationException {

        if (days <= 0) {
            throw new InvalidInputException("Days must be > 0.");
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

        // создаём аренду
        Rental rental = rentalRepo.create(
                new Rental(customer, car, startDate, days)
        );

        // делаем машину недоступной
        car.setAvailable(false);
        carRepo.update(car.getId(), car);

        return rental;
    }

    public List<Rental> getAll() throws DatabaseOperationException {
        return rentalRepo.getAll(customerRepo, carRepo);
    }

    public void closeRental(int rentalId)
            throws ResourceNotFoundException, DatabaseOperationException {

        boolean closed = rentalRepo.closeRental(rentalId);
        if (!closed) {
            throw new ResourceNotFoundException("Active rental not found.");
        }
    }
}
