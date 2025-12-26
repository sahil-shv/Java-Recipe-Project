package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database utility class for managing database connections
 */
public class DatabaseUtil {
    private static String DB_URL;
    private static String DB_USERNAME;
    private static String DB_PASSWORD;
    private static String DB_DRIVER;
    
    static {
        loadDatabaseConfig();
    }
    
    /**
     * Load database configuration from system properties or properties file
     */
    private static void loadDatabaseConfig() {
        // First try to get from system properties (for production deployment)
        DB_URL = System.getProperty("db.url");
        DB_USERNAME = System.getProperty("db.user");
        DB_PASSWORD = System.getProperty("db.password");
        
        // If system properties not found, load from properties file
        if (DB_URL == null || DB_USERNAME == null || DB_PASSWORD == null) {
            Properties props = new Properties();
            try (InputStream input = DatabaseUtil.class.getClassLoader()
                    .getResourceAsStream("db.properties")) {
                if (input != null) {
                    props.load(input);
                    DB_URL = props.getProperty("db.url");
                    DB_USERNAME = props.getProperty("db.username");
                    DB_PASSWORD = props.getProperty("db.password");
                    DB_DRIVER = props.getProperty("db.driver");
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to load database configuration", e);
            }
        }
        
        // Load MySQL driver
        try {
            if (DB_DRIVER != null) {
                Class.forName(DB_DRIVER);
            } else {
                Class.forName("com.mysql.cj.jdbc.Driver");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }
    
    /**
     * Get database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }
    
    /**
     * Close database connection safely
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Test database connection
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}