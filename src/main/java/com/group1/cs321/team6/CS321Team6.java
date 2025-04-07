package com.group1.cs321.team6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class CS321Team6 {

    public static void main(String[] args) {
        initDB();
        HashMap<String, Object> presets = new HashMap<>();

        presets.put("Equation", "-y");
        presets.put("x_0", 0.0);
        presets.put("y_0", 1.0);
        presets.put("xEnd", 4.0);
        presets.put("h", 0.1);
//        presets.put("nSteps", 5);
//        presets.put("minStep", 0.01);
//        presets.put("maxStep", 0.1);

        UserInput user = new UserInput(presets, "Cates", "password");
        HashMap<String, Object> validated_presets = user.getPresets();
        Factory factory = new Factory(validated_presets, user);

        IntegrationRunner integration_runner = new IntegrationRunner(factory);
        
        HashMap<String, Pair<List<Double>, List<Double>>> result = integration_runner.performIntegration(factory.createIntegrators());

        for (String key : result.keySet()) {
            Pair<List<Double>, List<Double>> value = result.get(key);
            System.out.println("Key: " + key + ", tValues: " + value.getFirst() + ", yValues: " + value.getSecond());
        }

        // Create the graph
        plotResults(result, presets);
    }

    private static void plotResults(HashMap<String, Pair<List<Double>, List<Double>>> result, HashMap<String, Object> presets) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        // For each integration method
        for (String method : result.keySet()) {
            XYSeries series = new XYSeries(method);
            Pair<List<Double>, List<Double>> data = result.get(method);
            List<Double> xValues = data.getFirst(); // tValues for Adams-Bashforth
            List<Double> yValues = data.getSecond(); // yValues

            // Add points to the series
            for (int i = 0; i < xValues.size() && i < yValues.size(); i++) {
                series.add(xValues.get(i), yValues.get(i));
            }
            dataset.addSeries(series);
        }

        // Create the chart
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Numerical Integration Results: " + presets.get("Equation"), // Chart title
            "t",                                                        // X-axis label (time)
            "y",                                                        // Y-axis label
            dataset                                                     // Data
        );

        // Display the chart
        ChartFrame frame = new ChartFrame("Integration Results", chart);
        frame.pack();
        frame.setVisible(true);
    }

    // Assuming initDB() is defined elsewhere
    private static void initDB() {
        // Placeholder for your database initialization
    }
}