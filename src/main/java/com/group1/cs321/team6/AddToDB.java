/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group1.cs321.team6;
import com.group1.cs321.team6.InputValidator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.HashMap;

/**
 *
 * @author Michael A
 */



public class AddToDB {
    
    private final HashMap<String, Object> inputMap;
    
    public AddToDB(HashMap<String, Object> inputMap) {
        this.inputMap = new HashMap<>(inputMap); 
    }
    /**
     * Returns false if data was unsuccessful when adding
     */
    public Boolean inputtingVals() throws SQLException {
    InputValidator validator = new InputValidator(inputMap);
    if (!validator.checkInput()) return false;

    StringBuilder columns = new StringBuilder();
    StringBuilder placeholders = new StringBuilder();
    for (String column : inputMap.keySet()) {
        columns.append(column).append(",");
        placeholders.append("?,");
    }

    String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", 
        "Equations",
        columns.substring(0, columns.length()-1),
        placeholders.substring(0, placeholders.length()-1)
    );

    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:your_db.db");
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        int index = 1;
        for (Object value : inputMap.values()) {
            // Convert all values to String explicitly
            pstmt.setString(index++, String.valueOf(value));
        }
        pstmt.executeUpdate();
    }
    return true;
    }
    
    // ****************************************************************************************
    public static void main(String[] args) {
        // Sample data to test the AddToDB class
        HashMap<String, Object> sampleData = new HashMap<>();
        sampleData.put("id", 123);
        sampleData.put("equation", "Test equation");
        sampleData.put("result", "Test result");

        // Create an instance of AddToDB with the sample data
        AddToDB addToDB = new AddToDB(sampleData);

        try {
            // Test the inputtingVals method
            boolean success = addToDB.inputtingVals();
            if (success) {
                System.out.println("Data inserted successfully!");
            } else {
                System.out.println("Data insertion failed!");
            }
        } catch (SQLException e) {
            System.err.println("An error occurred while inserting data: " + e.getMessage());
        }
    }
}


