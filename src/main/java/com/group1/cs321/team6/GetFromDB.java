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
 * 
 * This class returns the most recent addition to the Database.
 * Note this returns a hash map.
 */
public class GetFromDB {
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
     * This returns n number of rows from the database.
     * Note this returns a list of hashmaps.
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