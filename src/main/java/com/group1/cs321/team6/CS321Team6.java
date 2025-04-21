package com.group1.cs321.team6;

import java.util.HashMap;
import java.util.List;
import static com.group1.cs321.team6.CreateDatabase.initDB;
import java.sql.SQLException;

/**
 *This is the main class for team 6's , the Lunch club's, project. This class 
 * implements various libraries in order to create a differential equation solver
 * that effectively solves and shows different the accuracy of different methods.
 */
public class CS321Team6 {

    /**
     * Default constructor for CS321Team6
     */
    public CS321Team6(){
    // Explicit constructor for documentation
    }
    
    /**
     * Entry point for the differential equation solver application.
     * @throws SQLException If database initialization fails
     */
    public static void main(String[] args) throws SQLException {
        initDB();
        
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
    }
}
