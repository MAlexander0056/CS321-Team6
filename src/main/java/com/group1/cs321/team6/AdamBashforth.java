package com.group1.cs321.team6;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math4.legacy.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math4.legacy.ode.nonstiff.AdamsBashforthIntegrator;
import org.apache.commons.math4.legacy.ode.sampling.StepHandler;
import org.apache.commons.math4.legacy.ode.sampling.StepInterpolator;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * A class that implements the Adams-Bashforth method for numerical integration of ODEs.
 * This is a multi-step predictor method that uses previous points to estimate the next step.
 */
public class AdamBashforth implements Integrator {
    // Member variables
    private final String equation;         // ODE right-hand side, e.g., "x + y"
    private final double t0;               // Initial time (renamed from x0 for convention)
    private final double y0;               // Initial value of dependent variable (y)
    private final double tEnd;             // End time (renamed from xEnd)
    private final int nSteps;              // Number of previous steps to use (quirky parameter)
    private final double minStep;          // Minimum step size for adaptive control
    private final double maxStep;          // Maximum step size for adaptive control
    private final Expression expression;   // Parsed mathematical expression
    private List<Double> tValues;          // Stores time values (instead of xValues)
    private List<Double> yValues;          // Stores y values of the solution

    /**
     * Constructor to initialize the integrator with the ODE and parameters.
     *
     * @param equation The ODE right-hand side as a string (e.g., "x + y" for dy/dt = x + y)
     * @param t0       Initial time value
     * @param y0       Initial y value
     * @param tEnd     Final time value where integration stops
     * @param nSteps   Number of previous steps to use in the Adams-Bashforth method (e.g., 2, 3, 4)
     * @param minStep  Minimum allowable step size
     * @param maxStep  Maximum allowable step size
     */
    public AdamBashforth(String equation, double t0, double y0, double tEnd, int nSteps, double minStep, double maxStep) {
        this.equation = equation;
        this.t0 = t0;
        this.y0 = y0;
        this.tEnd = tEnd;
        this.nSteps = nSteps;
        this.minStep = minStep;
        this.maxStep = maxStep;
        // Build the expression with variables "x" and "y" (keeping naming consistent with input)
        this.expression = new ExpressionBuilder(equation).variables("x", "y").build();
    }

    /**
     * Performs the Adams-Bashforth integration and populates the solution lists.
     */
    @Override
    public void integrate() {
        // Initialize solution lists
        tValues = new ArrayList<>();
        yValues = new ArrayList<>();

        // Define the ODE using an inner class
        FirstOrderDifferentialEquations ode = new ODE();

        // Create the Adams-Bashforth integrator with the specified parameters
        AdamsBashforthIntegrator integrator = new AdamsBashforthIntegrator(nSteps, minStep, maxStep, 1e-6, 1e-4);

        // Add a step handler to collect solution points
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {
                // Add initial point
                tValues.add(t0);
                yValues.add(y0[0]);
            }

            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) {
                // Add the current step's point
                double t = interpolator.getCurrentTime();
                double[] y = interpolator.getInterpolatedState();
                tValues.add(t);
                yValues.add(y[0]);
            }
        });

        // Set up initial conditions and perform integration
        double[] yStart = new double[]{y0};
        double[] yEnd = new double[1];
        integrator.integrate(ode, t0, yStart, tEnd, yEnd);
    }

    /**
     * Returns the list of time values from the solution.
     *
     * @return List of time values
     */
    @Override
    public List<Double> getXValues() {
        return tValues; // Renamed to reflect time-based convention
    }

    /**
     * Returns the list of y values from the solution.
     *
     * @return List of y values
     */
    @Override
    public List<Double> getYValues() {
        return yValues;
    }

    /**
     * Private inner class implementing the ODE dy/dt = f(t, y).
     */
    private class ODE implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 1; // Single first-order ODE
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            // Set variables in the expression and evaluate
            expression.setVariable("x", t); // Using "x" to match input equation convention
            expression.setVariable("y", y[0]);
            yDot[0] = expression.evaluate();
        }
    }
}