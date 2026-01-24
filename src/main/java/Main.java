import exception.*;
import model.Customer;
import model.Car;
import service.CustomerService;
import service.CarService;
import service.RentalService;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        CustomerService customerService = new CustomerService();
        CarService carService = new CarService();
        RentalService rentalService = new RentalService();

        System.out.println("CAR RENTAL CONSOLE-API ");

        try {
            // CUSTOMERS
            System.out.println("\n-- Customers --");
            customerService.getAll()
                    .forEach(c -> System.out.println(c.describe()));

            Customer newCustomer = customerService.create(
                    new Customer("DemoUser", "+7-700-999-88-77"));
            System.out.println("[OK] Created customer: " + newCustomer.describe());

            // CARS
            System.out.println("\n-- Cars --");
            carService.getAll()
                    .forEach(c -> System.out.println(c.describe()));

            // RENTAL
            System.out.println("\n-- Create rental --");
            var rental = rentalService.create(
                    newCustomer.getId(), 1, LocalDate.now(), 3);
            System.out.println("[OK] Rental created: " + rental.describe());

            //  BUSINESS RULE (ERROR)
            System.out.println("\n-- Try second rental for same customer --");
            rentalService.create(
                    newCustomer.getId(), 2, LocalDate.now(), 2);

        } catch (DuplicateResourceException e) {
            System.out.println("[ERROR] Duplicate: " + e.getMessage());
        } catch (InvalidInputException e) {
            System.out.println("[ERROR] Invalid input: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            System.out.println("[ERROR] Not found: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.out.println("[ERROR] Database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
