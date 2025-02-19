/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group1.cs321.team6;
import com.group1.cs321.team6.UserInput;
import java.lang.Object;
import org.apache.commons.math4.legacy.ode.AbstractIntegrator;
import org.apache.commons.math4.legacy.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math4.legacy.ode.nonstiff.EulerIntegrator;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 *
 * @author cates
 */

/**
 * 
 *  An example numerical method
 */
public class EulersMethod {
    /**
     * Constructor:
     * @param equationInput The user will input an equation as a string.
     *      E.g. sin(t), e^t, etc...
     * @param t Initial time value]
     * @param y Initial y value
     * 
     */
    EulersMethod(String equationInput, double t, double[] y) {
        
    }
    
    /**
     * 
     *  Handles this.equationInput such that it can be used as a mathematical expression
     *  @return Expression Comes from exp4j library, which handles math string parsing
     *
     */
    private Expression ParseExpression () {
        return null;
    }
    
    /**
     * 
     * @param yDot Computes the derivative. 
     *  Necessary for Apache Commons Euler integrator
     */
    private void computeDerivatives (Expression parsedEquation, double[] yDot) {
        
    }
    
    private int getDimension() {
        return 1;
    }
    
    /**
     * 
     * Returns a set of values necessary to plot the approximation
     */
    private double[] integrateFunction () {
        return null;
    }
}
