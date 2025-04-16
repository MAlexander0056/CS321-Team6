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
}


