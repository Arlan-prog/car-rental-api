package repository;

import exception.DatabaseOperationException;
import model.Car;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarRepository {

    public Car create(Car car) throws DatabaseOperationException {
        String sql = "INSERT INTO cars(name, daily_price, is_available) VALUES (?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, car.getName());
            ps.setDouble(2, car.getDailyPrice());
            ps.setBoolean(3, car.isAvailable());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    car.setId(rs.getInt("id"));
                    return car;
                }
                throw new DatabaseOperationException("Failed to create car (no id returned).", null);
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while creating car.", e);
        }
    }

    public List<Car> getAll() throws DatabaseOperationException {
        String sql = "SELECT id, name, daily_price, is_available FROM cars ORDER BY id";
        List<Car> result = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new Car(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("daily_price"),
                        rs.getBoolean("is_available")
                ));
            }
            return result;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while reading cars.", e);
        }
    }

    public Car getById(int id) throws DatabaseOperationException {
        String sql = "SELECT id, name, daily_price, is_available FROM cars WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Car(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("daily_price"),
                            rs.getBoolean("is_available")
                    );
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while reading car by id.", e);
        }
    }

    public boolean update(int id, Car car) throws DatabaseOperationException {
        String sql = "UPDATE cars SET name = ?, daily_price = ?, is_available = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, car.getName());
            ps.setDouble(2, car.getDailyPrice());
            ps.setBoolean(3, car.isAvailable());
            ps.setInt(4, id);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while updating car.", e);
        }
    }

    public boolean delete(int id) throws DatabaseOperationException {
        String sql = "DELETE FROM cars WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while deleting car.", e);
        }
    }
}
