package com.group1.cs321.team6;
import org.apache.commons.math4.legacy.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math4.legacy.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math4.legacy.ode.sampling.StepHandler;
import org.apache.commons.math4.legacy.ode.sampling.StepInterpolator;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import java.util.ArrayList;
import java.util.List;

public class RungeKuttaOrder4 {
    // Instance variables
    private final String equation;         // ODE right-hand side (e.g., "x + y")
    private final double x0;               // Initial x value
    private final double y0;               // Initial y value
    private final double xEnd;             // End x value
    private final double h;                // Step size
    private final Expression expression;   // Parsed ODE expression
    private List<Double> xValues;          // Solution x points
    private List<Double> yValues;          // Solution y points

    /**
     * Constructor to initialize the integrator.
     *
     * @param equation The ODE right-hand side as a string
     * @param x0       Initial x value
     * @param y0       Initial y value
     * @param xEnd     Final x value
     * @param h        Step size
     */
    public RungeKuttaOrder4(String equation, double x0, double y0, double xEnd, double h) {
        this.equation = equation;
        this.x0 = x0;
        this.y0 = y0;
        this.xEnd = xEnd;
        this.h = h;
        // Parse the equation string into an evaluable expression
        this.expression = new ExpressionBuilder(equation).variables("x", "y").build();
    }

    /**
     * Performs the fourth-order Runge-Kutta integration.
     */
    public void integrate() {
        // Initialize lists to store solution points
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();

        // Define the ODE
        FirstOrderDifferentialEquations ode = new ODE();

        // Create the Runge-Kutta integrator with fixed step size
        ClassicalRungeKuttaIntegrator integrator = new ClassicalRungeKuttaIntegrator(h);

        // Add a step handler to collect solution points at each step
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {
                // Store initial point
                xValues.add(t0);
                yValues.add(y0[0]);
            }

            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) {
                // Store the point at the end of the current step
                double t = interpolator.getCurrentTime();
                double[] y = interpolator.getInterpolatedState();
                xValues.add(t);
                yValues.add(y[0]);
            }
        });

        // Set initial conditions and integrate
        double[] yStart = new double[]{y0};
        double[] yEnd = new double[1];
        integrator.integrate(ode, x0, yStart, xEnd, yEnd);
    }

    /**
     * Gets the list of y values from the solution.
     *
     * @return List of y values
     */
    public List<Double> getYValues() {
        return yValues;
    }

    /**
     * Inner class defining the ODE dy/dx = f(x, y).
     */
    private class ODE implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 1; // Single first-order ODE
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            // Evaluate the ODE at the current (x, y)
            expression.setVariable("x", t);
            expression.setVariable("y", y[0]);
            yDot[0] = expression.evaluate();
        }
    }

    /**
     * Static method to execute the entire workflow: create integrator, perform integration, and return y-values.
     *
     * @param equation The ODE right-hand side as a string
     * @param x0       Initial x value
     * @param y0       Initial y value
     * @param xEnd     Final x value
     * @param h        Step size
     * @return List of y values from the solution
     */
    public static List<Double> executeSolver(String equation, double x0, double y0, double xEnd, double h) {
        // Step 1: Create a dynamic object (instance) using the constructor
        RungeKuttaOrder4 integrator = new RungeKuttaOrder4(equation, x0, y0, xEnd, h);
        
        // Step 2: Execute the integration
        integrator.integrate();
        
        // Step 3: Return the y-values
        return integrator.getYValues();
    }
}