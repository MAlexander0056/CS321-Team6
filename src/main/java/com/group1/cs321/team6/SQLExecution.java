package com.group1.cs321.team6;
import com.group1.cs321.team6.SQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author(s): Cates, Cate
 */

/**
 * Responsible for executing prepared statements, creating data structure to hold user presets,
 * or INSERT user presets into the database.
 */
public class SQLExecution {
    /**
     * Has a connection instance as a member variable
     */
    private SQLConnection sqlConnection;
    
    /**
     * Constructor initializes with a SQLConnection object
     * 
     * @param sqlConnectionInput SQLConnection object to use
     */
    SQLExecution(SQLConnection sqlConnectionInput) {
        this.sqlConnection = sqlConnectionInput;
    }
    
    /**
     * Inserts a new user into the database
     * 
     * @param username Username
     * @param password Password
     * @return boolean indicating if insertion was successful
     */
    public boolean insertUser(String username, String password) {
        try {
            Connection conn = sqlConnection.getConnection();
            
            // First check if the username already exists
            String checkQuery = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    return false; // User already exists
                }
            }
            
            // Insert the new user
            String insertQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password); // In a real app, use password hashing!
                
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Authenticates a user in the database
     * 
     * @param username Username
     * @param password Password
     * @return boolean indicating if authentication was successful
     */
    public boolean authenticateUser(String username, String password) {
        try {
            Connection conn = sqlConnection.getConnection();
            
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password); // In a real app, use password hashing!
                
                ResultSet rs = pstmt.executeQuery();
                return rs.next(); // True if user found
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Saves an equation setup to the database
     * 
     * @param setupName Name for the equation setup
     * @return boolean indicating if save was successful
     */
    public boolean saveEquationSetup(String setupName) {
        return sqlConnection.saveEquationSetup(setupName);
    }
    
    /**
     * Retrieves all saved setups for a user
     * 
     * @param username Username
     * @return List of setup names
     */
    public List<String> getSavedSetups(String username) {
        List<String> setupNames = new ArrayList<>();
        
        try {
            Connection conn = sqlConnection.getConnection();
            
            String query = "SELECT setup_name FROM equation_setups WHERE username = ? ORDER BY saved_date DESC";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, username);
                
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    setupNames.add(rs.getString("setup_name"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving saved setups: " + e.getMessage());
        }
        
        return setupNames;
    }
    
    /**
     * Retrieves a specific equation setup
     * 
     * @param username Username
     * @param setupName Name of the setup to retrieve
     * @return HashMap containing the setup parameters or null if not found
     */
    public HashMap<String, Object> retrieveEquationSetup(String username, String setupName) {
        return sqlConnection.loadEquationSetup(username, setupName);
    }
    
    /**
     * Closes the database connection
     */
    public void closeConnection() {
        sqlConnection.closeConnection();
    }
}
