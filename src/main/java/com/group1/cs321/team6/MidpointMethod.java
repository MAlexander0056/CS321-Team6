package com.group1.cs321.team6;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math4.legacy.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math4.legacy.ode.nonstiff.MidpointIntegrator;
import org.apache.commons.math4.legacy.ode.sampling.StepHandler;
import org.apache.commons.math4.legacy.ode.sampling.StepInterpolator;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * A class that implements the midpoint method for numerical integration of ODEs.
 * This is a second-order Runge-Kutta method that uses an intermediate step to improve accuracy
 * over first-order methods like Euler's method.
 */
public class MidpointMethod implements Integrator {
    // Member variables
    private final String equation;         // ODE right-hand side, e.g., "x + y"
    private final double x0;               // Initial value of independent variable (x)
    private final double y0;               // Initial value of dependent variable (y)
    private final double xEnd;             // End value of independent variable (x)
    private final double h;                // Step size for integration
    private final Expression expression;   // Parsed mathematical expression
    private List<Double> xValues;          // Stores x values of the solution
    private List<Double> yValues;          // Stores y values of the solution

    /**
     * Constructor to initialize the integrator with the ODE and parameters.
     *
     * @param equation The ODE right-hand side as a string (e.g., "x + y" for dy/dx = x + y)
     * @param x0       Initial x value
     * @param y0       Initial y value
     * @param xEnd     Final x value where integration stops
     * @param h        Step size for the midpoint method
     */
    public MidpointMethod(String equation, double x0, double y0, double xEnd, double h) {
        this.equation = equation;
        this.x0 = x0;
        this.y0 = y0;
        this.xEnd = xEnd;
        this.h = h;
        // Build the expression with variables "x" and "y"
        this.expression = new ExpressionBuilder(equation).variables("x", "y").build();
    }

    /**
     * Performs the midpoint integration and populates the solution lists.
     */
    @Override
    public void integrate() {
        // Initialize solution lists
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();

        // Define the ODE using an inner class
        FirstOrderDifferentialEquations ode = new ODE();

        // Create the midpoint integrator with the specified step size
        MidpointIntegrator integrator = new MidpointIntegrator(h);

        // Add a step handler to collect solution points
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {
                // Add initial point
                xValues.add(t0);
                yValues.add(y0[0]);
            }

            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) {
                // Add the current step's point
                double t = interpolator.getCurrentTime();
                double[] y = interpolator.getInterpolatedState();
                xValues.add(t);
                yValues.add(y[0]);
            }
        });

        // Set up initial conditions and perform integration
        double[] yStart = new double[]{y0};
        double[] yEnd = new double[1];
        integrator.integrate(ode, x0, yStart, xEnd, yEnd);
    }

    /**
     * Returns the list of x values from the solution.
     *
     * @return List of x values
     */
    @Override
    public List<Double> getXValues() {
        return xValues;
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
     * Private inner class implementing the ODE dy/dx = f(x, y).
     */
    private class ODE implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 1; // Single first-order ODE
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            // Set variables in the expression and evaluate
            expression.setVariable("x", t);
            expression.setVariable("y", y[0]);
            yDot[0] = expression.evaluate();
        }
    }
}