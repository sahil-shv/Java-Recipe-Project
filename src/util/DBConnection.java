package util;

import utils.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_HOST = System.getenv("DB_HOST") != null 
        ? System.getenv("DB_HOST") : "localhost";
    
    private static final String DB_PORT = System.getenv("DB_PORT") != null 
        ? System.getenv("DB_PORT") : "5432";
    
    private static final String DB_NAME = System.getenv("DB_NAME") != null 
        ? System.getenv("DB_NAME") : "recipeshare";
    
    private static final String USERNAME = System.getenv("DB_USERNAME") != null 
        ? System.getenv("DB_USERNAME") : "postgres";
    
    private static final String PASSWORD = System.getenv("DB_PASSWORD") != null 
        ? System.getenv("DB_PASSWORD") : "password";
    
    private static final String URL = "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?ssl=true&sslmode=require";
    
    private static DBConnection instance;
    
    private DBConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            Logger.info("PostgreSQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            Logger.error("PostgreSQL Driver not found", e);
            throw new SQLException("PostgreSQL Driver not found", e);
        }
    }
    
    public static synchronized DBConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Logger.debug("Database connection established");
        return conn;
    }
}
