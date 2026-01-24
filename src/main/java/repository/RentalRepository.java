package repository;

import exception.DatabaseOperationException;
import model.Car;
import model.Customer;
import model.Rental;
import utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalRepository {

    public Rental create(Rental rental) throws DatabaseOperationException {
        String sql = """
            INSERT INTO rentals(customer_id, car_id, start_date, days, status)
            VALUES (?, ?, ?, ?, ?) RETURNING id
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rental.getCustomer().getId());
            ps.setInt(2, rental.getCar().getId());
            ps.setDate(3, Date.valueOf(rental.getStartDate()));
            ps.setInt(4, rental.getDays());
            ps.setString(5, rental.getStatus());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    rental.setId(rs.getInt("id"));
                    return rental;
                }
                throw new DatabaseOperationException("Failed to create rental.", null);
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while creating rental.", e);
        }
    }

    public List<Rental> getAll(CustomerRepository customerRepo, CarRepository carRepo)
            throws DatabaseOperationException {

        String sql = "SELECT id, customer_id, car_id, start_date, days, status FROM rentals ORDER BY id";
        List<Rental> result = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Customer customer = customerRepo.getById(rs.getInt("customer_id"));
                Car car = carRepo.getById(rs.getInt("car_id"));

                result.add(new Rental(
                        rs.getInt("id"),
                        customer,
                        car,
                        rs.getDate("start_date").toLocalDate(),
                        rs.getInt("days"),
                        rs.getString("status")
                ));
            }
            return result;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while reading rentals.", e);
        }
    }

    public boolean closeRental(int id) throws DatabaseOperationException {
        String sql = "UPDATE rentals SET status = 'FINISHED' WHERE id = ? AND status = 'ACTIVE'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while closing rental.", e);
        }
    }

    public boolean hasActiveRentalForCustomer(int customerId) throws DatabaseOperationException {
        String sql = "SELECT 1 FROM rentals WHERE customer_id = ? AND status = 'ACTIVE'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while checking active rental.", e);
        }
    }
}
