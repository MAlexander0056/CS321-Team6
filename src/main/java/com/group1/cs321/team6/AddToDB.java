/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group1.cs321.team6;
import static com.group1.cs321.team6.DbConfig.JDBC_URL;
import com.group1.cs321.team6.InputValidator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
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
        "equations",
        columns.substring(0, columns.length()-1),
        placeholders.substring(0, placeholders.length()-1)
    );

    try (Connection conn = DriverManager.getConnection(JDBC_URL);
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
//    public static void main(String[] args) {
//    HashMap<String, Object> sampleData = new HashMap<>();
//    sampleData.put("equation", "y_prime = t^2 + 3y");  // Removed special characters
//    sampleData.put("t0", 1.0);                       // String for validation
//    sampleData.put("y0", 1.0);                       // String for validation
//    sampleData.put("tEnd", 5.0);                     // String for validation
//    sampleData.put("nSteps", 100);                   // String for validation
//    sampleData.put("minStep", 0.01);                 // String for validation
//    sampleData.put("maxStep", 1.0);                  // String for validation
//
//    AddToDB addToDB = new AddToDB(sampleData);
//    
//    try {
//        // Test insertion
//        boolean success = addToDB.inputtingVals();
//        System.out.println(success ? "Insert successful!" : "Insert failed!");
//
//        // Add verification section
//        System.out.println("\nVerifying database contents:");
//        try (Connection conn = DriverManager.getConnection(JDBC_URL);
//             Statement stmt = conn.createStatement()) {
//            
//            // Print all tables (debugging)
//            ResultSet tables = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table'");
//            while (tables.next()) {
//                System.out.println("Found table: " + tables.getString("name"));
//            }
//
//            // Print equations table contents
//            ResultSet rs = stmt.executeQuery("SELECT * FROM Equations");
//            ResultSetMetaData meta = rs.getMetaData();
//            
//            // Print column headers
//            for (int i = 1; i <= meta.getColumnCount(); i++) {
//                System.out.print(meta.getColumnName(i) + "\t");
//            }
//            System.out.println();
//            
//            // Print rows
//            while (rs.next()) {
//                for (int i = 1; i <= meta.getColumnCount(); i++) {
//                    System.out.print(rs.getString(i) + "\t");
//                }
//                System.out.println();
//            }
//        }
//    } catch (SQLException e) {
//        System.err.println("Database error: " + e.getMessage());
//    }
//}
// *********************************************************************************************************************
}


