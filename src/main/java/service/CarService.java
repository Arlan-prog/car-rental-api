package service;

import exception.DatabaseOperationException;
import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import model.Car;
import repository.CarRepository;

import java.util.List;

public class CarService {

    private final CarRepository repo = new CarRepository();

    public Car create(Car car)
            throws InvalidInputException, DatabaseOperationException {

        car.validate();
        return repo.create(car);
    }

    public List<Car> getAll() throws DatabaseOperationException {
        return repo.getAll();
    }

    public Car getById(int id)
            throws ResourceNotFoundException, DatabaseOperationException {

        if (id <= 0) {
            throw new ResourceNotFoundException("Car id must be positive.");
        }

        Car car = repo.getById(id);
        if (car == null) {
            throw new ResourceNotFoundException("Car with id=" + id + " not found.");
        }
        return car;
    }

    public Car update(int id, Car car)
            throws InvalidInputException, ResourceNotFoundException, DatabaseOperationException {

        car.validate();

        Car existing = repo.getById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Car with id=" + id + " not found.");
        }

        boolean ok = repo.update(id, car);
        if (!ok) {
            throw new DatabaseOperationException("Update failed (no rows affected).", null);
        }

        return repo.getById(id);
    }

    public void delete(int id)
            throws ResourceNotFoundException, DatabaseOperationException {

        Car existing = repo.getById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Car with id=" + id + " not found.");
        }

        boolean ok = repo.delete(id);
        if (!ok) {
            throw new DatabaseOperationException("Delete failed (no rows affected).", null);
        }
    }
}
