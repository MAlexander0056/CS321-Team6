package com.group1.cs321.team6;

import java.util.HashMap;
import java.util.List;
/**
 *
 * @author Admin
 */

public class CS321Team6 {

    public static void main(String[] args) {
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
    }
    
    // Assuming initDB() is defined elsewhere
    private static void initDB() {
        // Placeholder for your database initialization
    }
}
