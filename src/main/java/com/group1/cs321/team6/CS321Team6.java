package com.group1.cs321.team6;

import java.util.HashMap;
import java.util.List;
import static com.group1.cs321.team6.CreateDatabase.initDB;
import static com.group1.cs321.team6.DbConfig.JDBC_URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author Admin
 */

public class CS321Team6 {

    public static void main(String[] args) throws SQLException {
        initDB();

// TODO Remove before submission. Exaple for bashforth
//        HashMap<String, Object> presets = new HashMap<>();

//        presets.put("Equation", "-y");
//        presets.put("x_0", 0.0);
//        presets.put("y_0", 1.0);
//        presets.put("xEnd", 4.0);
//        presets.put("h", 0.1);
//        presets.put("nSteps", 5);
//        presets.put("minStep", 0.01);
//        presets.put("maxStep", 0.1);

        
        // Open the main window that accepts the user's inputs and returns
        // a hash map
        Gui guiWindow = new Gui();        
        HashMap<String, Object> presets = guiWindow.CreateMainWindow();
        
        UserInput user = new UserInput(presets, "Cates", "password");
        HashMap<String, Object> validated_presets = user.getPresets();
        Factory factory = new Factory(validated_presets, user);

        IntegrationRunner integration_runner = new IntegrationRunner(factory);
        
        HashMap<String, Pair<List<Double>, List<Double>>> result = integration_runner.performIntegration(factory.createIntegrators());

        for (String key : result.keySet()) {
            Pair<List<Double>, List<Double>> value = result.get(key);
            System.out.println("Key: " + key + ", tValues: " + value.getFirst() + ", yValues: " + value.getSecond());
        }

        // Create the graph
        guiWindow.CreateSolutionWindow(result, presets);
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL);
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery("SELECT * FROM Equations")) {

    ResultSetMetaData meta = rs.getMetaData();
    int columnCount = meta.getColumnCount();

    // Print column headers
    for (int i = 1; i <= columnCount; i++) {
        System.out.print(meta.getColumnName(i) + "\t");
    }
    System.out.println();

    // Print each row
    while (rs.next()) {
        for (int i = 1; i <= columnCount; i++) {
            System.out.print(rs.getString(i) + "\t");
        }
        System.out.println();
    }
    } catch (SQLException e) {
    System.err.println("Error reading from database: " + e.getMessage());
    }
    }
    
}
