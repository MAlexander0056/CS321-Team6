/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group1.cs321.team6;

import static com.group1.cs321.team6.DbConfig.JDBC_URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Michael A
 */
public class GetFromDB {

    public HashMap<String, Object> getOneRow() {
        HashMap<String, Object> singleRow; 
        
        try {
        System.out.println("\nVerifying database contents:");
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
            Statement stmt = conn.createStatement()) {
            
            // Print equations table contents
            ResultSet rs = stmt.executeQuery("SELECT * FROM Equations");
            ResultSetMetaData meta = rs.getMetaData();
            
            // Print rows
            while (rs.next()) {
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }
        }
    } catch (SQLException e) {
        System.err.println("Database error: " + e.getMessage());
    }
    }
    
}
