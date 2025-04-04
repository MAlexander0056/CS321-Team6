package com.group1.cs321.team6;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * A factory class that creates numerical integrator objects based on the specified method.
 */
public class Factory {
    // Member variables
    private final HashMap<String, Object> givenParameters; // Parameters for integration
    private final UserInput userToRetrieve;               // User input for additional retrieval

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
     * Creates a list of Integrator objects based on the specified methods.
     *
     * @param methodNames The names of the integration methods to use (e.g., "Euler", "RK4")
     * @return A List of Integrator objects
     * @throws IllegalArgumentException if required parameters are missing or invalid
     */
    public List<Integrator> createIntegrators(List<String> methodNames) {
        // Define the required keys
        String[] requiredKeys = {"Equation", "x_0", "y_0", "xEnd", "h"};

        // Check if all required keys are present with non-null values
        for (String key : requiredKeys) {
            if (!givenParameters.containsKey(key) || givenParameters.get(key) == null) {
                throw new IllegalArgumentException("Missing or null parameter: " + key);
            }
        }

        // Retrieve parameters from the HashMap
        String equation = (String) givenParameters.get("Equation");
        double x0 = ((Number) givenParameters.get("x_0")).doubleValue();
        double y0 = ((Number) givenParameters.get("y_0")).doubleValue();
        double xEnd = ((Number) givenParameters.get("xEnd")).doubleValue();
        double h = ((Number) givenParameters.get("h")).doubleValue();

        // Validate parameters
        if (h <= 0) {
            throw new IllegalArgumentException("Step size h must be positive");
        }
        if (xEnd <= x0) {
            throw new IllegalArgumentException("xEnd must be greater than x_0");
        }

        // Create the list of integrators
        List<Integrator> integrators = new ArrayList<>();
        for (String method : methodNames) {
            switch (method.toLowerCase()) {
                case "euler":
                    integrators.add(new EulersMethod(equation, x0, y0, xEnd, h));
                    break;
                case "rk4":
                    integrators.add(new RungeKuttaOrder4(equation, x0, y0, xEnd, h));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown integration method: " + method);
            }
        }

        return integrators;
    }
}