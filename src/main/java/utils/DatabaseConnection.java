package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/car_rental_db";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String url = System.getenv().getOrDefault("DB_URL", DEFAULT_URL);
        String user = System.getenv().getOrDefault("DB_USER", "postgres");
        String password = System.getenv("DB_PASSWORD");
        if (password == null || password.isBlank()) {
            throw new SQLException("DB_PASSWORD must be set in the environment.");
        }
        return DriverManager.getConnection(url, user, password);
    }
}
