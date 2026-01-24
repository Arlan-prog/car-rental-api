package repository;

import exception.DatabaseOperationException;
import model.Customer;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {

    public Customer create(Customer customer) throws DatabaseOperationException {
        String sql = "INSERT INTO customers(name, phone) VALUES (?, ?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    customer.setId(rs.getInt("id"));
                    return customer;
                }
                throw new DatabaseOperationException("Failed to create customer (no id returned).", null);
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while creating customer.", e);
        }
    }

    public List<Customer> getAll() throws DatabaseOperationException {
        String sql = "SELECT id, name, phone FROM customers ORDER BY id";
        List<Customer> result = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone")
                ));
            }
            return result;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while reading customers.", e);
        }
    }

    public Customer getById(int id) throws DatabaseOperationException {
        String sql = "SELECT id, name, phone FROM customers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("phone")
                    );
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while reading customer by id.", e);
        }
    }

    public Customer getByPhone(String phone) throws DatabaseOperationException {
        String sql = "SELECT id, name, phone FROM customers WHERE phone = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("phone")
                    );
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while reading customer by phone.", e);
        }
    }

    public boolean update(int id, Customer customer) throws DatabaseOperationException {
        String sql = "UPDATE customers SET name = ?, phone = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setInt(3, id);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while updating customer.", e);
        }
    }

    public boolean delete(int id) throws DatabaseOperationException {
        String sql = "DELETE FROM customers WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while deleting customer.", e);
        }
    }
}
