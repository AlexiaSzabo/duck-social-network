package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides a single shared connection for the entire application.
 * This avoids the overhead of opening/closing DB connections repeatedly.
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/ducknetwork";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    private static Connection connection;

    private DatabaseConnection() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
        return connection;
    }
}
