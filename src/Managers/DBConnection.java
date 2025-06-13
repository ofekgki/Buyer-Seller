package Managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/ecommers_system";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234qwer";

    public static Connection getConnection() {
            try {
                Class.forName("org.postgresql.Driver");
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                return connection;
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to connect to database.");
            }


    }
}