package com.group1.cs321.team6;

import org.apache.commons.math4.legacy.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math4.legacy.ode.FirstOrderIntegrator;
import org.apache.commons.math4.legacy.ode.sampling.StepHandler;
import org.apache.commons.math4.legacy.ode.sampling.StepInterpolator;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractODESolver implements ODESolver {
    protected final String equation;
    protected final double x0;
    protected final double y0;
    protected final double xEnd;
    protected final double h;
    protected final Expression expression;
    protected List<Double> xValues;
    protected List<Double> yValues;

    public AbstractODESolver(String equation, double x0, double y0, double xEnd, double h) {
        this.equation = equation;
        this.x0 = x0;
        this.y0 = y0;
        this.xEnd = xEnd;
        this.h = h;
        this.expression = new ExpressionBuilder(equation).variables("x", "y").build();
    }

    @Override
    public void integrate() {
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();

        FirstOrderDifferentialEquations ode = new ODE();
        FirstOrderIntegrator integrator = createIntegrator();

        integrator.addStepHandler(new StepHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {
                xValues.add(t0);
                yValues.add(y0[0]);
            }

            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLast) {
                double t = interpolator.getCurrentTime();
                double[] y = interpolator.getInterpolatedState();
                xValues.add(t);
                yValues.add(y[0]);
            }
        });

        double[] yStart = new double[]{y0};
        double[] yEnd = new double[1];
        integrator.integrate(ode, x0, yStart, xEnd, yEnd);
    }

    @Override
    public List<Double> getYValues() {
        return yValues;
    }

    protected abstract FirstOrderIntegrator createIntegrator();

    private class ODE implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            expression.setVariable("x", t);
            expression.setVariable("y", y[0]);
            yDot[0] = expression.evaluate();
        }
    }
}