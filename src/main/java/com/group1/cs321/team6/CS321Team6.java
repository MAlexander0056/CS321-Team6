/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.group1.cs321.team6;
import static com.group1.cs321.team6.CreateDatabase.initDB;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Admin
 */

public class CS321Team6 {

    public static void main(String[] args) {
        initDB();
        
        // Open the main window that accepts the user's inputs and returns
        // a hash map
        Gui mainWindow = new Gui();        
        HashMap<String, Object> presets = mainWindow.CreateMainWindow();
        
        UserInput user = new UserInput(presets, "Cates", "password");
        // Some validation then happens with presets
        
        HashMap<String, Object> validated_presets = user.getPresets();
        
        Factory factory = new Factory(validated_presets, user);
        
        List<String> integrators_to_create = new ArrayList<>();
        
        integrators_to_create.add("euler");
        integrators_to_create.add("rk4");
        
        List<Integrator> integrator_objects = factory.createIntegrators(integrators_to_create);
        
        IntegrationRunner integration_runner = new IntegrationRunner(factory);
        HashMap<String, List<Double>> result = new HashMap<>();
        result = integration_runner.performIntegration(integrators_to_create);
        
        for (String key : result.keySet()) {
            List<Double> value = result.get(key);
            System.out.println("Key: " + key + ", Value: " + value);
        }
        
        // Create the graph
        plotResults(result, presets);
    }
    
    private static void plotResults(HashMap<String, List<Double>> result, HashMap<String, Object> presets) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        double x0 = ((Number) presets.get("x_0")).doubleValue();
        double xEnd = ((Number) presets.get("xEnd")).doubleValue();
        double h = ((Number) presets.get("h")).doubleValue();

        // For each integration method
        for (String method : result.keySet()) {
            XYSeries series = new XYSeries(method);
            List<Double> yValues = result.get(method);
            double x = x0;
            
            // Add points to the series
            for (Double y : yValues) {
                series.add(x, y);
                x += h;
            }
            dataset.addSeries(series);
        }

        // Create the chart
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Numerical Integration Results: " + presets.get("Equation"), // Chart title
            "x",                                                        // X-axis label
            "y",                                                        // Y-axis label
            dataset                                                     // Data
        );

        // Display the chart
        ChartFrame frame = new ChartFrame("Integration Results", chart);
        frame.pack();
        frame.setVisible(true);

    }
}
