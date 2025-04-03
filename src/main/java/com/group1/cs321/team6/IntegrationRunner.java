package com.group1.cs321.team6;

import java.util.HashMap;
import java.util.List;

/**
 * A class that orchestrates numerical integration using a Factory and returns the results.
 */
public class IntegrationRunner {
    private final Factory factory;

    /**
     * Constructor to initialize the runner with a Factory.
     *
     * @param factory The Factory to use for creating integrators
     */
    public IntegrationRunner(Factory factory) {
        this.factory = factory;
    }

    /**
     * Performs numerical integration using the specified methods.
     *
     * @param methodNames The names of the integration methods to use (e.g., "Euler", "RK4")
     * @return A HashMap mapping method names to their y-value results
     */
    public HashMap<String, List<Double>> performIntegration(List<String> methodNames) {
        HashMap<String, List<Double>> result = new HashMap<>();

        // Create integrators using the factory
        List<Integrator> integrators = factory.createIntegrators(methodNames);

        // Perform integration for each integrator
        for (Integrator integrator : integrators) {
            integrator.integrate();
            String methodName = integrator.getClass().getSimpleName();
            // Map the class name to a more readable name
            String displayName = methodName.equals("EulersMethod") ? "Euler's method" : "RK4";
            result.put(displayName, integrator.getYValues());
        }

        return result;
    }
}
