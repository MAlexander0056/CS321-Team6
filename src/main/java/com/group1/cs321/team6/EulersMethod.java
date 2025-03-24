package com.group1.cs321.team6;

import org.apache.commons.math4.legacy.ode.FirstOrderIntegrator;
import org.apache.commons.math4.legacy.ode.nonstiff.EulerIntegrator;
import java.util.List;

public class EulersMethod extends AbstractODESolver {
    public EulersMethod(String equation, double x0, double y0, double xEnd, double h) {
        super(equation, x0, y0, xEnd, h);
    }

    @Override
    protected FirstOrderIntegrator createIntegrator() {
        return new EulerIntegrator(h);
    }

    // Optionally, keep the static method for backward compatibility
    public static List<Double> executeSolver(String equation, double x0, double y0, double xEnd, double h) {
        EulersMethod integrator = new EulersMethod(equation, x0, y0, xEnd, h);
        integrator.integrate();
        return integrator.getYValues();
    }
}