/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group1.cs321.team6;

import static com.group1.cs321.team6.DbConfig.JDBC_URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael A
 */

/**
 * This class provides the functionality to retrieve previous configurations
 * from the database.
 */
public class GetFromDB {
    /**
     * This function returns the most recent addition to the database.
     * 
     * @return A hash map containing the most recent row from the database.
     * @throws SQLException 
     */
    public static HashMap<String, Object> getMostRecentEquation() throws SQLException {
    String query = "SELECT * FROM Equations ORDER BY id DESC LIMIT 1";
    
    try (Connection conn = DriverManager.getConnection(JDBC_URL);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        if (rs.next()) {
            HashMap<String, Object> rowMap = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object value = rs.getObject(i);
                rowMap.put(columnName, value);
            }
            return rowMap;
        }
        return null;
    }
    }

    /**
     * This function returns n number of rows from the database.
     * 
     * @param limit The number of rows to return from the database
     * @return A list of hash maps containing the desired number of rows from
     * the database.
     * @throws SQLException
     */
    public static List<HashMap<String, Object>> getRecentEquations(int limit) throws SQLException {
    List<HashMap<String, Object>> equations = new ArrayList<>();
    String query = "SELECT * FROM Equations ORDER BY id DESC LIMIT ?";
    
    try (Connection conn = DriverManager.getConnection(JDBC_URL);
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        pstmt.setInt(1, limit);  // Set the limit parameter
        try (ResultSet rs = pstmt.executeQuery()) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (rs.next()) {
                HashMap<String, Object> rowMap = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowMap.put(metaData.getColumnName(i), rs.getObject(i));
                }
                equations.add(rowMap);
            }
        }
    }
    return equations;
}

}