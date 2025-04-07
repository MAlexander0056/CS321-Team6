package com.group1.cs321.team6;
import com.group1.cs321.team6.UserInput;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Author(s): Cates, Cate
 */

/**
 * Responsible for establishing connectivity with the database
 * and handling basic database operations.
 */
public class SQLConnection {
    // Database URL
    private static String URL;
    private UserInput user;
    private Connection connection;
    
    /**
     * Constructor initializes the connection URL and user object
     * 
     * @param someUser UserInput object containing user data
     */
    SQLConnection(UserInput someUser) {
        this.user = someUser;
        
        // Set up the database URL
        File dbDir = new File(System.getProperty("user.home"), ".EquationSolver");
        File dbFile = new File(dbDir, "StoredEquations.db");
        URL = "jdbc:sqlite:" + dbFile.getAbsolutePath().replace("\\", "/");
        
        try {
            databaseConnection();
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection: " + e.getMessage());
        }
    }
    
    /**
     * Returns the UserInput instance
     * 
     * @return UserInput instance
     */
    public UserInput getUser() {
        return this.user;
    }
    
    /**
     * Attempts to establish a connection to the database
     * 
     * @throws SQLException If connection fails
     */
    private void databaseConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
        }
    }
    
    /**
     * Gets the current database connection
     * 
     * @return Connection object
     * @throws SQLException If connection cannot be established
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            databaseConnection();
        }
        return connection;
    }
   
    /**
     * Closes the database connection
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Saves equation setup to the database
     * 
     * @param setupName Name of the setup
     * @return boolean indicating if save was successful
     */
    public boolean saveEquationSetup(String setupName) {
        try {
            // Ensure we have a connection
            if (connection == null || connection.isClosed()) {
                databaseConnection();
            }
            
            // Get user data
            String username = user.getUsername();
            HashMap<String, Object> presets = user.getPresets();
            
            // Prepare SQL statement
            String sql = "INSERT INTO equation_setups (username, setup_name, equation, init_x, init_y, " +
                    "final_x, step_size, use_euler, use_rk4) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, setupName);
                pstmt.setString(3, (String) presets.get("Equation"));
                pstmt.setDouble(4, ((Number) presets.get("x_0")).doubleValue());
                pstmt.setDouble(5, ((Number) presets.get("y_0")).doubleValue());
                pstmt.setDouble(6, ((Number) presets.get("xEnd")).doubleValue());
                pstmt.setDouble(7, ((Number) presets.get("h")).doubleValue());
                pstmt.setBoolean(8, (Boolean) presets.get("Euler"));
                pstmt.setBoolean(9, (Boolean) presets.get("RK4"));
                
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error saving equation setup: " + e.getMessage());
            return false;
        }
    }
   
    /**
     * Loads an equation setup from the database
     * 
     * @param username Username
     * @param setupName Name of the setup to load
     * @return HashMap with loaded parameters or null if not found
     */
    public HashMap<String, Object> loadEquationSetup(String username, String setupName) {
        try {
            // Ensure we have a connection
            if (connection == null || connection.isClosed()) {
                databaseConnection();
            }
            
            // Prepare SQL statement
            String sql = "SELECT * FROM equation_setups WHERE username = ? AND setup_name = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, setupName);
                
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    HashMap<String, Object> setup = new HashMap<>();
                    setup.put("Equation", rs.getString("equation"));
                    setup.put("x_0", rs.getDouble("init_x"));
                    setup.put("y_0", rs.getDouble("init_y"));
                    setup.put("xEnd", rs.getDouble("final_x"));
                    setup.put("h", rs.getDouble("step_size"));
                    setup.put("Euler", rs.getBoolean("use_euler"));
                    setup.put("RK4", rs.getBoolean("use_rk4"));
                    return setup;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading equation setup: " + e.getMessage());
        }
        
        return null;
    }
}
