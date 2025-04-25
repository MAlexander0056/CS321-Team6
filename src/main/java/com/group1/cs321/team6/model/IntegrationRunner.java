package com.group1.cs321.team6.model;

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
     * @param integrators The names of the integration methods to use (e.g., "Euler", "RK4", "adamsbashforth")
     *@return A HashMap mapping method names to a Pair of x-values (t-values) and y-value results
     */
    public HashMap<String, Pair<List<Double>, List<Double>>> performIntegration(List<Integrator> integrators) {
        HashMap<String, Pair<List<Double>, List<Double>>> result = new HashMap<>();
        for (Integrator integrator : integrators) {
            integrator.integrate();
            String methodName = integrator.getClass().getSimpleName();
            String displayName = mapMethodName(methodName);
            result.put(displayName, new Pair<>(integrator.getXValues(), integrator.getYValues()));
        }
        return result;
    }

    private String mapMethodName(String methodName) {
        switch (methodName) {
            case "EulersMethod": return "Euler's method";
            case "MidpointMethod": return "Midpoint method";
            case "RungeKuttaOrder4": return "RK4";
            case "AdamBashforth": return "Adams-Bashforth Method";
            case "ExactSolution": return "Exact Solution";
            default: return methodName;
        }
    }
}