package com.group1.cs321.team6;

import java.util.List;

/**
 * Interface for numerical integrators to solve ODEs.
 */
public interface Integrator {
    /**
     * Performs the numerical integration and populates the solution.
     */
    void integrate();

    /**
     * Returns the list of x values from the solution.
     *
     * @return List of x values
     */
    List<Double> getXValues();

    /**
     * Returns the list of y values from the solution.
     *
     * @return List of y values
     */
    List<Double> getYValues();
}
