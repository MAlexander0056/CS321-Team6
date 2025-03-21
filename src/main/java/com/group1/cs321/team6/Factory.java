package com.group1.cs321.team6;

import com.group1.cs321.team6.EulersMethod;
import com.group1.cs321.team6.RungeKuttaOrder4;
import com.group1.cs321.team6.SQLExecution;
import com.group1.cs321.team6.UserInput;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * A factory class that performs numerical integration using Euler's method and Runge-Kutta order 4
 * based on parameters provided in a HashMap.
 */
public class Factory {
    // Member variables
    private HashMap<String, Object> givenParameters; // Adjusted to Object to store String and Integer
    private UserInput userToRetrieve;

    /**
     * Constructor to initialize the factory with parameters and user input.
     *
     * @param parameters A HashMap containing integration parameters
     * @param user       A UserInput object for potential additional input retrieval
     */
    public Factory(HashMap<String, Object> parameters, UserInput user) {
        this.givenParameters = parameters;
        this.userToRetrieve = user;
    }

    /**
     * Performs numerical integration if all required parameters are present with non-null values.
     * Returns a list of lists containing y-values from Euler's method and Runge-Kutta order 4.
     *
     * @return A List<List<Double>> with two inner lists: y-values from Euler's method and Runge-Kutta order 4,
     *         or an empty list if required parameters are missing or null
     */
    public HashMap<String, List<Double>> performIntegration() {
        // Define the required keys
        String[] requiredKeys = {"Equation", "x_0", "y_0", "xEnd", "h"};

        // Check if all required keys are present with non-null values
        for (String key : requiredKeys) {
            if (!givenParameters.containsKey(key) || givenParameters.get(key) == null) {
                return new HashMap<>(); // Return empty HashMap if any key is missing or null
            }
        }

        // Retrieve parameters from the HashMap
        String equation = (String) givenParameters.get("Equation");
        double x0 = ((Integer) givenParameters.get("x_0")).doubleValue();
        double y0 = ((Integer) givenParameters.get("y_0")).doubleValue();
        double xEnd = ((Integer) givenParameters.get("xEnd")).doubleValue();
        double h = ((Integer) givenParameters.get("h")).doubleValue();

        // Perform integrations using Euler's method and Runge-Kutta order 4
        List<Double> yValsEuler = EulersMethod.executeSolver(equation, x0, y0, xEnd, h);
        List<Double> yValsRK4 = RungeKuttaOrder4.executeSolver(equation, x0, y0, xEnd, h);

        // Create and populate the HashMap
        HashMap<String, List<Double>> result = new HashMap<>();
        result.put("Euler's method", yValsEuler);
        result.put("RK4", yValsRK4);

        return result;
    }
}