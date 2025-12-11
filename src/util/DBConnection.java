package util;

import utils.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_HOST = System.getenv("DB_HOST") != null 
        ? System.getenv("DB_HOST") 
        : (System.getenv("DATABASE_URL") != null ? extractHostFromUrl(System.getenv("DATABASE_URL")) : "localhost");
    private static final String DB_PORT = System.getenv("DB_PORT") != null 
        ? System.getenv("DB_PORT") 
        : (System.getenv("DATABASE_URL") != null ? extractPortFromUrl(System.getenv("DATABASE_URL")) : "3306");
    private static final String DB_NAME = System.getenv("DB_NAME") != null 
        ? System.getenv("DB_NAME") 
        : (System.getenv("DATABASE_URL") != null ? extractDatabaseFromUrl(System.getenv("DATABASE_URL")) : "recipeshare");
    private static final String USERNAME = System.getenv("DB_USERNAME") != null 
        ? System.getenv("DB_USERNAME") 
        : (System.getenv("DATABASE_URL") != null ? extractUserFromUrl(System.getenv("DATABASE_URL")) : "root");
    private static final String PASSWORD = System.getenv("DB_PASSWORD") != null 
        ? System.getenv("DB_PASSWORD") 
        : (System.getenv("DATABASE_URL") != null ? extractPasswordFromUrl(System.getenv("DATABASE_URL")) : "password");
    
    private static final String URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    
    private static String extractHostFromUrl(String url) {
        // Parse DATABASE_URL format: mysql://user:password@host:port/database
        try {
            if (url.startsWith("mysql://")) {
                String withoutProtocol = url.substring(8);
                int atIndex = withoutProtocol.indexOf("@");
                int colonIndex = withoutProtocol.indexOf(":", atIndex + 1);
                return withoutProtocol.substring(atIndex + 1, colonIndex);
            }
        } catch (Exception e) {
            Logger.error("Error parsing DATABASE_URL", e);
        }
        return "localhost";
    }
    
    private static String extractPortFromUrl(String url) {
        try {
            if (url.startsWith("mysql://")) {
                String withoutProtocol = url.substring(8);
                int atIndex = withoutProtocol.indexOf("@");
                int colonIndex = withoutProtocol.indexOf(":", atIndex + 1);
                int slashIndex = withoutProtocol.indexOf("/", colonIndex);
                return withoutProtocol.substring(colonIndex + 1, slashIndex);
            }
        } catch (Exception e) {
            Logger.error("Error parsing DATABASE_URL port", e);
        }
        return "3306";
    }
    
    private static String extractDatabaseFromUrl(String url) {
        try {
            if (url.startsWith("mysql://")) {
                int lastSlash = url.lastIndexOf("/");
                int questionMark = url.indexOf("?");
                if (questionMark > 0) {
                    return url.substring(lastSlash + 1, questionMark);
                }
                return url.substring(lastSlash + 1);
            }
        } catch (Exception e) {
            Logger.error("Error parsing DATABASE_URL database", e);
        }
        return "recipeshare";
    }
    
    private static String extractUserFromUrl(String url) {
        try {
            if (url.startsWith("mysql://")) {
                String withoutProtocol = url.substring(8);
                int colonIndex = withoutProtocol.indexOf(":");
                int atIndex = withoutProtocol.indexOf("@");
                return withoutProtocol.substring(0, Math.min(colonIndex, atIndex));
            }
        } catch (Exception e) {
            Logger.error("Error parsing DATABASE_URL user", e);
        }
        return "root";
    }
    
    private static String extractPasswordFromUrl(String url) {
        try {
            if (url.startsWith("mysql://")) {
                String withoutProtocol = url.substring(8);
                int colonIndex = withoutProtocol.indexOf(":");
                int atIndex = withoutProtocol.indexOf("@");
                if (colonIndex < atIndex) {
                    return withoutProtocol.substring(colonIndex + 1, atIndex);
                }
            }
        } catch (Exception e) {
            Logger.error("Error parsing DATABASE_URL password", e);
        }
        return "password";
    }

    private static DBConnection instance;

    private DBConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Logger.info("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            Logger.error("MySQL Driver not found", e);
            throw new SQLException("MySQL Driver not found", e);
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
