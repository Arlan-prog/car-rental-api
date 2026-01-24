package service;

import exception.DatabaseOperationException;
import exception.DuplicateResourceException;
import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import model.Customer;
import repository.CustomerRepository;

import java.util.List;

public class CustomerService {

    private final CustomerRepository repo = new CustomerRepository();

    public Customer create(Customer customer)
            throws InvalidInputException, DuplicateResourceException, DatabaseOperationException {

        customer.validate();

        // бизнес-правило: телефон уникальный (и так в БД UNIQUE, но мы делаем красиво на service layer)
        Customer existing = repo.getByPhone(customer.getPhone());
        if (existing != null) {
            throw new DuplicateResourceException("Customer with phone '" + customer.getPhone() + "' already exists.");
        }

        return repo.create(customer);
    }

    public List<Customer> getAll() throws DatabaseOperationException {
        return repo.getAll();
    }

    public Customer getById(int id)
            throws ResourceNotFoundException, DatabaseOperationException {

        if (id <= 0) {
            throw new ResourceNotFoundException("Customer id must be positive.");
        }

        Customer c = repo.getById(id);
        if (c == null) {
            throw new ResourceNotFoundException("Customer with id=" + id + " not found.");
        }
        return c;
    }

    public Customer update(int id, Customer customer)
            throws InvalidInputException, DuplicateResourceException, ResourceNotFoundException, DatabaseOperationException {

        customer.validate();

        // проверка что существует
        Customer existingById = repo.getById(id);
        if (existingById == null) {
            throw new ResourceNotFoundException("Customer with id=" + id + " not found.");
        }

        // проверка что телефон не занят другим человеком
        Customer existingByPhone = repo.getByPhone(customer.getPhone());
        if (existingByPhone != null && existingByPhone.getId() != id) {
            throw new DuplicateResourceException("Phone '" + customer.getPhone() + "' is already used by another customer.");
        }

        boolean ok = repo.update(id, customer);
        if (!ok) {
            throw new DatabaseOperationException("Update failed (no rows affected).", null);
        }

        // возвращаем обновлённого
        return repo.getById(id);
    }

    public void delete(int id)
            throws ResourceNotFoundException, DatabaseOperationException {

        Customer existing = repo.getById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Customer with id=" + id + " not found.");
        }

        boolean ok = repo.delete(id);
        if (!ok) {
            throw new DatabaseOperationException("Delete failed (no rows affected).", null);
        }
    }
}
