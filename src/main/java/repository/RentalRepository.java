package repository;

import exception.DatabaseOperationException;
import model.Car;
import model.Customer;
import model.Rental;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalRepository {

    public Rental create(Rental rental) throws DatabaseOperationException {
        String reserveCar = "UPDATE cars SET is_available = FALSE WHERE id = ? AND is_available = TRUE";
        String insertRental = """
                INSERT INTO rentals(customer_id, car_id, start_date, days, status)
                VALUES (?, ?, ?, ?, ?) RETURNING id
                """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement reserve = conn.prepareStatement(reserveCar);
                 PreparedStatement insert = conn.prepareStatement(insertRental)) {
                reserve.setInt(1, rental.getCar().getId());
                if (reserve.executeUpdate() != 1) {
                    throw new SQLException("Car is no longer available.");
                }

                insert.setInt(1, rental.getCustomer().getId());
                insert.setInt(2, rental.getCar().getId());
                insert.setDate(3, Date.valueOf(rental.getStartDate()));
                insert.setInt(4, rental.getDays());
                insert.setString(5, rental.getStatus());
                try (ResultSet result = insert.executeQuery()) {
                    if (!result.next()) {
                        throw new SQLException("No rental id was returned.");
                    }
                    rental.setId(result.getInt("id"));
                }
                conn.commit();
                return rental;
            } catch (SQLException error) {
                conn.rollback();
                throw error;
            }
        } catch (SQLException error) {
            throw new DatabaseOperationException("Database error while creating rental.", error);
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
        } catch (SQLException error) {
            throw new DatabaseOperationException("Database error while reading rentals.", error);
        }
    }

    public boolean closeRental(int id) throws DatabaseOperationException {
        String sql = """
                WITH completed AS (
                    UPDATE rentals
                    SET status = 'FINISHED'
                    WHERE id = ? AND status = 'ACTIVE'
                    RETURNING car_id
                )
                UPDATE cars
                SET is_available = TRUE
                WHERE id IN (SELECT car_id FROM completed)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException error) {
            throw new DatabaseOperationException("Database error while closing rental.", error);
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
        } catch (SQLException error) {
            throw new DatabaseOperationException("Database error while checking active rental.", error);
        }
    }
}
