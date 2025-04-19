package com.group1.cs321.team6;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import com.group1.cs321.team6.AddToDB;
import java.sql.SQLException;

 /**
  * This class creates both of the windows required for our project: the 
  * introduction window that prompts the user for inputs, and the graph window
  * that displays the solution to the inputted ODE 
  */
public class Gui {
    
    private String equation = "";
    private double initX = 0.0;
    private double initY = 0.0;
    private double finalX = 0.0;
    private double step = 0.0;
    private int nSteps = 0;
    private double minStep = 0.0;
    private double maxStep = 0.0;
    private boolean eulerSelected = false;
    private boolean rk4Selected = false;
    private boolean midpointSelected = false;
    private boolean adamBashSelected = false;

    /**
     * Window for introduction page. Requests information needed to create the
     * appropriate integrators.
     * 
     * @return A hash map containing the user's inputs
     */
    public HashMap<String, Object> CreateMainWindow() throws SQLException{
        // Create the main window
        JFrame frame = new JFrame("Team 6: ODE Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        
        // Create intro panel and text
        JPanel introPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel introLabel = new JLabel(
            "<html><body style='width:450px; font-family:Serif; font-size:18pt; "
            + "font-style:italic; color:blue;'>Welcome to our Ordinary "
            + "Differential Equation solver! Enter any non-stiff initial value "
            + "problem, and choose from the available numerical methods "
            + "to see a graph of the solution.</body></html>");
        
        // Create panels for each input
        JPanel equationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel initXPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel initYPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel finalXPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel stepPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel nStepPanel = new JPanel (new FlowLayout(FlowLayout.LEFT));
        JPanel minStepPanel = new JPanel (new FlowLayout(FlowLayout.LEFT));
        JPanel maxStepPanel = new JPanel (new FlowLayout(FlowLayout.LEFT));
        JPanel methodPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        // Create labels for each input
        JLabel equationLabel = new JLabel(
            "<html><span style='font-family:Monospaced; font-size:14pt; "
            + "color:red;'>Equation:</span></html>"
        );
        JLabel initXLabel = new JLabel(
            "<html><span style='font-family:Monospaced; font-size:14pt; "
            + "color:red;'>Initial X:</span></html>"
            );
        JLabel initYLabel = new JLabel(
            "<html><span style='font-family:Monospaced; font-size:14pt; "
            + "color:red;'>Initial Y:</span></html>"
        );
        JLabel finalXLabel = new JLabel(
            "<html><span style='font-family:Monospaced; font-size:14pt; "
            + "color:red;'>Final X:</span></html>"
            );
        JLabel stepLabel = new JLabel(
            "<html><span style='font-family:Monospaced; font-size:14pt; "
            + "color:red;'>Step Size:</span></html>"
            );
        JLabel nStepLabel = new JLabel(
            "<html><span style='font-family:Monospaced; font-size:14pt; "
            + "color:red;'>Number of Previous Steps:</span></html>"
            );
        JLabel minStepLabel = new JLabel(
            "<html><span style='font-family:Monospaced; font-size:14pt; "
            + "color:red;'>Minimum Step Size:</span></html>"
            );
        JLabel maxStepLabel = new JLabel(
            "<html><span style='font-family:Monospaced; font-size:14pt; "
            + "color:red;'>Maximum Step Size:</span></html>"
            );
        
        // Create text fields for each input
        JTextField equationField = new JTextField("Enter your ODE.");
        JTextField initXField = new JTextField("Enter an initial X value.");
        JTextField initYField = new JTextField("Enter an initial Y value.");
        JTextField finalXField = new JTextField("Enter a final X value.");
        JTextField stepField = new JTextField("Enter a step size.");
        JTextField nStepField = new JTextField("Enter the number of previous steps to use.");
        JTextField minStepField = new JTextField("Enter the minimum step size.");
        JTextField maxStepField = new JTextField("Enter the maximum step size.");
        
        // Create panels for section headers
        JPanel requiredInputsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel extraInputsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel availMethodsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        // Create labels for section headers 
        JLabel requiredInputsLabel = new JLabel(
            "<html><span style='font-family:SansSerif; font-size:16pt; "
            + "font-weight:bold; color:#444444;'>Required Inputs:</span></html>");
        JLabel extraInputsLabel = new JLabel(
            "<html><span style='font-family:SansSerif; font-size:16pt; "
            + "font-weight:bold; color:#444444;'>Additional Inputs for "
            + "Adams-Bashforth:</span></html>");
        JLabel availMethodsLabel = new JLabel(
            "<html><span style='font-family:SansSerif; font-size:16pt; "
            + "font-weight:bold; color:#444444;'>Select at least one of the "
            + "following methods:</span></html>");
        
        // Create check boxes for each solver option
        JCheckBox eulerBox = new JCheckBox("<html><span style='font-family:Serif;"
            + "font-size:14pt; color:#228B22;'>Euler</span></html>");
        JCheckBox rk4Box = new JCheckBox("<html><span style='font-family:Serif;"
            + "font-size:14pt; color:#228B22;'>4th Order Runge-Kutta</span></html>");
        JCheckBox midBox = new JCheckBox("<html><span style='font-family:Serif;"
            + "font-size:14pt; color:#228B22;'>Midpoint</span></html>");
        JCheckBox adamBashBox = new JCheckBox("<html><span style='font-family:Serif;"
            + "font-size:14pt; color:#228B22;'>Adam-Bashforth</span></html>");
        
        // Create a button to save user inputs
        JButton saveButton = new JButton("<html><u>Plot</u></html>");
        saveButton.setMaximumSize(new Dimension(90, 30));
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create a flag to signal when inputs are saved
        final boolean[] saved = {false};
        
        // Create panels for each pair of labels and field, as well as check boxes
        equationPanel.add(equationLabel);
        equationPanel.add(equationField);
        initXPanel.add(initXLabel);
        initXPanel.add(initXField);
        initYPanel.add(initYLabel);
        initYPanel.add(initYField);
        finalXPanel.add(finalXLabel);
        finalXPanel.add(finalXField);
        stepPanel.add(stepLabel);
        stepPanel.add(stepField);
        nStepPanel.add(nStepLabel);
        nStepPanel.add(nStepField);
        minStepPanel.add(minStepLabel);
        minStepPanel.add(minStepField);
        maxStepPanel.add(maxStepLabel);
        maxStepPanel.add(maxStepField);
        methodPanel.add(eulerBox);
        methodPanel.add(midBox);
        methodPanel.add(rk4Box);
        methodPanel.add(adamBashBox);
        introPanel.add(introLabel);
        
        // Adding labels to section header panels
        requiredInputsPanel.add(requiredInputsLabel);
        extraInputsPanel.add(extraInputsLabel);
        availMethodsPanel.add(availMethodsLabel);
        
        // Create an ActionListener to save user inputs and populate the HashMap
        saveButton.addActionListener(event -> {
                // Verify that the user inputted appropriate values and throw
                // an exception if not
                try {
                    equation = equationField.getText();
                    initX = Double.parseDouble(initXField.getText());
                    initY = Double.parseDouble(initYField.getText());
                    finalX = Double.parseDouble(finalXField.getText());
                    step = Double.parseDouble(stepField.getText());
                    
                    eulerSelected = eulerBox.isSelected();
                    rk4Selected = rk4Box.isSelected();
                    midpointSelected = midBox.isSelected();
                    adamBashSelected = adamBashBox.isSelected();

                    saved[0] = true; // Mark that inputs are saved
                    frame.dispose(); // Close the frame after saving inputs
                

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid "
                            + "numeric values for each of the required and "
                            + "additional inputs, and ensure that at least "
                            + "one of the available integration methods is selected.");
                }
                
                // If Adams-Bashforth parameters were not specified, then set
                // them to 0
                try {
                    nSteps = Integer.parseInt(nStepField.getText());
                    minStep = Double.parseDouble(minStepField.getText());
                    maxStep = Double.parseDouble(maxStepField.getText());                    
                } catch (NumberFormatException ex) {
                    nSteps = 0;
                    minStep = 0.0;
                    maxStep = 0.0;
                }
        });
        
        // Add the panels and button to the window
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(introPanel);
        frame.add(requiredInputsPanel);
        frame.add(equationPanel);
        frame.add(initXPanel);
        frame.add(initYPanel);
        frame.add(finalXPanel);
        frame.add(stepPanel);
        frame.add(extraInputsPanel);
        frame.add(nStepPanel);
        frame.add(minStepPanel);
        frame.add(maxStepPanel);
        frame.add(availMethodsPanel);
        frame.add(methodPanel);
        frame.add(saveButton);
        
        frame.setVisible(true);
        frame.pack();
                
        // Wait until the user presses save
        while (!saved[0]) {
            try {
                Thread.sleep(100); // Wait and check the flag periodically
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        
        // Return the HashMap once inputs are collected
        HashMap<String, Object> inputs = new HashMap<>();
        inputs.put("Equation", equation);
        inputs.put("x_0", initX);
        inputs.put("y_0", initY);
        inputs.put("xEnd", finalX);
        inputs.put("h", step);
        inputs.put("nSteps", nSteps);
        inputs.put("minStep", minStep);
        inputs.put("maxStep", maxStep);
        inputs.put("Euler", eulerSelected);
        inputs.put("RK4", rk4Selected);
        inputs.put("Midpoint", midpointSelected);
        inputs.put("Adam_Bashforth", adamBashSelected);
        
        AddToDB returnVal = new AddToDB(inputs);
        returnVal.inputtingVals();
        
        return inputs;
    }    
    
    /**
     * Window for solution page.Displays the graph of each method selected
     * by the user in the same window.
     * 
     * @param result Hash map containing the integration results
     * @param presets Hash map containing the user's original inputs
     */
    void CreateSolutionWindow(HashMap<String, Pair<List<Double>, List<Double>>> result, HashMap<String, Object> presets){
        XYSeriesCollection dataset = new XYSeriesCollection();

        // For each integration method
        for (String method : result.keySet()) {
            XYSeries series = new XYSeries(method);
            Pair<List<Double>, List<Double>> data = result.get(method);
            List<Double> xValues = data.getFirst();  // tValues for Adams-Bashforth
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
            "t",                                                         // X-axis label (time)
            "y",                                                         // Y-axis label
            dataset                                                      // Data
        );

        // Display the chart
        ChartFrame frame = new ChartFrame("Integration Results", chart);
        frame.pack();
        frame.setVisible(true);

    }
}