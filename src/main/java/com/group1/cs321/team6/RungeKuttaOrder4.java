package com.group1.cs321.team6;

import org.apache.commons.math4.legacy.ode.FirstOrderIntegrator;
import org.apache.commons.math4.legacy.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import java.util.List;

public class RungeKuttaOrder4 extends AbstractODESolver {
    public RungeKuttaOrder4(String equation, double x0, double y0, double xEnd, double h) {
        super(equation, x0, y0, xEnd, h);
    }

    @Override
    protected FirstOrderIntegrator createIntegrator() {
        return new ClassicalRungeKuttaIntegrator(h);
    }

    // Optionally, keep the static method for backward compatibility
    public static List<Double> executeSolver(String equation, double x0, double y0, double xEnd, double h) {
        RungeKuttaOrder4 integrator = new RungeKuttaOrder4(equation, x0, y0, xEnd, h);
        integrator.integrate();
        return integrator.getYValues();
    }
}