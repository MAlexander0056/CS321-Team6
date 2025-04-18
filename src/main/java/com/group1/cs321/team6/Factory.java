package com.group1.cs321.team6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A factory class that creates numerical integrator objects based on the specified method or inferred from parameters.
 */
public class Factory {
    private final HashMap<String, Object> givenParameters;
    private final UserInput userToRetrieve;

    public Factory(HashMap<String, Object> parameters, UserInput user) {
        this.givenParameters = parameters;
        this.userToRetrieve = user;
    }

    /**
     * Creates a list of Integrator objects based on explicitly specified methods.
     *
     * @param methodNames The names of the integration methods to use (e.g., "euler", "rk4", "adamsbashforth")
     * @return A List of Integrator objects
     * @throws IllegalArgumentException if required parameters are missing or invalid
     */
    public List<Integrator> createIntegrators(List<String> methodNames) {
        return createIntegratorsInternal(methodNames);
    }

    /**
     * Creates a list of Integrator objects inferred from the provided parameters.
     *
     * @return A List of Integrator objects based on parameter analysis
     * @throws IllegalArgumentException if no valid parameter set is provided
     */
    public List<Integrator> createIntegrators() {
        List<String> inferredMethods = inferIntegratorTypes();
        return createIntegratorsInternal(inferredMethods);
    }

    /**
     * Internal method to create integrators based on a list of method names.
     */
    private List<Integrator> createIntegratorsInternal(List<String> methodNames) {
        String[] requiredKeys = {"Equation", "x_0", "y_0", "xEnd", "h"};
        String[] bashforthKeys = {"Equation", "x_0", "y_0", "xEnd", "nSteps", "minStep", "maxStep"};

        boolean standardKeysValid = checkKeys(requiredKeys);
        boolean bashforthKeysValid = checkKeys(bashforthKeys);

        if (!standardKeysValid && !bashforthKeysValid) {
            throw new IllegalArgumentException("No valid parameter set provided. Required: " +
                String.join(", ", requiredKeys) + " or " + String.join(", ", bashforthKeys));
        }

        String equation = (String) givenParameters.get("Equation");
        double x0 = ((Number) givenParameters.get("x_0")).doubleValue();
        double y0 = ((Number) givenParameters.get("y_0")).doubleValue();
        double xEnd = ((Number) givenParameters.get("xEnd")).doubleValue();

        List<Integrator> integrators = new ArrayList<>();
        for (String method : methodNames) {
            switch (method.toLowerCase()) {
                case "euler":
                    if (!standardKeysValid) {
                        throw new IllegalArgumentException("Euler method requires: " + String.join(", ", requiredKeys));
                    }
                    double hEuler = ((Number) givenParameters.get("h")).doubleValue();
                    validateStandardParams(hEuler, x0, xEnd);
                    integrators.add(new EulersMethod(equation, x0, y0, xEnd, hEuler));
                    break;
                case "rk4":
                    if (!standardKeysValid) {
                        throw new IllegalArgumentException("RK4 method requires: " + String.join(", ", requiredKeys));
                    }
                    double hRK4 = ((Number) givenParameters.get("h")).doubleValue();
                    validateStandardParams(hRK4, x0, xEnd);
                    integrators.add(new RungeKuttaOrder4(equation, x0, y0, xEnd, hRK4));
                    break;
                case "midpoint":
                    if (!standardKeysValid) {
                        throw new IllegalArgumentException("Midpoint method requires: " + String.join(", ", requiredKeys));
                    }
                    double hMidpoint = ((Number) givenParameters.get("h")).doubleValue();
                    validateStandardParams(hMidpoint, x0, xEnd);
                    integrators.add(new MidpointMethod(equation, x0, y0, xEnd, hMidpoint));
                    break;
                case "adamsbashforth":
                    if (!bashforthKeysValid) {
                        throw new IllegalArgumentException("Adams_Bashforth method requires: " + String.join(", ", bashforthKeys));
                    }
                    int nSteps = ((Number) givenParameters.get("nSteps")).intValue();
                    double minStep = ((Number) givenParameters.get("minStep")).doubleValue();
                    double maxStep = ((Number) givenParameters.get("maxStep")).doubleValue();
                    validateBashforthParams(nSteps, minStep, maxStep, x0, xEnd);
                    integrators.add(new AdamBashforth(equation, x0, y0, xEnd, nSteps, minStep, maxStep));
                    break;
                case "exact":
                    if (!standardKeysValid) {
                        throw new IllegalArgumentException("Exact method requires: " + String.join(", ", requiredKeys));
                    }
                    double hExact = ((Number) givenParameters.get("h")).doubleValue();
                    validateStandardParams(hExact, x0, xEnd);
                    integrators.add(new ExactSolution(equation, x0, y0, xEnd));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown integration method: " + method);
            }
        }
        return integrators;
    }

    /**
     * Infers which integrator types to create based on the parameters provided.
     */
    private List<String> inferIntegratorTypes() {
        List<String> methods = new ArrayList<>();
        boolean hasStandardKeys = checkKeys(new String[]{"Equation", "x_0", "y_0", "xEnd", "h"});
        boolean hasBashforthKeys = checkKeys(new String[]{"Equation", "x_0", "y_0", "xEnd", "nSteps", "minStep", "maxStep"});

        if (hasStandardKeys) {
            // Add all methods selected by the user. Only exact solution is graphed
            // by default
            methods.add("exact");
            if (this.givenParameters.get("Euler").equals(true)) {
                methods.add("euler");
            }
            if (this.givenParameters.get("RK4").equals(true)) {
            methods.add("rk4");
            }
            if (this.givenParameters.get("Midpoint").equals(true)) {
            methods.add("midpoint");
            }
        }
        if (hasBashforthKeys && this.givenParameters.get("Adam_Bashforth").equals(true)) {
            methods.add("adamsbashforth");
        }
        if (methods.isEmpty()) {
            throw new IllegalArgumentException("Unable to infer integrator type from parameters.");
        }
        return methods;
    }

    /**
     * Checks if all required keys are present and non-null in givenParameters.
     */
    private boolean checkKeys(String[] keys) {
        for (String key : keys) {
            if (!givenParameters.containsKey(key) || givenParameters.get(key) == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates parameters for standard fixed-step methods.
     */
    private void validateStandardParams(double h, double x0, double xEnd) {
        if (h <= 0) throw new IllegalArgumentException("Step size h must be positive");
        if (xEnd <= x0) throw new IllegalArgumentException("xEnd must be greater than x_0");
    }

    /**
     * Validates parameters for Adams-Bashforth.
     */
    private void validateBashforthParams(int nSteps, double minStep, double maxStep, double x0, double xEnd) {
        if (nSteps <= 0) throw new IllegalArgumentException("nSteps must be positive");
        if (minStep <= 0 || maxStep <= 0) throw new IllegalArgumentException("minStep and maxStep must be positive");
        if (minStep > maxStep) throw new IllegalArgumentException("minStep must be less than or equal to maxStep");
        if (xEnd <= x0) throw new IllegalArgumentException("xEnd must be greater than x_0");
    }
}