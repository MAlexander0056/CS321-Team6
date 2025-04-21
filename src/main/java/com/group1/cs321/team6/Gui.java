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
import java.sql.SQLException;
import java.util.ArrayList;
import static com.group1.cs321.team6.GetFromDB.getRecentEquations;

/**
 * This class creates both of the windows required for our project: the 
 * introduction window that prompts the user for inputs, and the graph window
 * that displays the solution to the inputted ODE.
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
     * @throws SQLException
     */
    public HashMap<String, Object> CreateMainWindow() throws SQLException{
        // Create the main window
        JFrame frame = new JFrame("Team 6: ODE Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create intro panel and text
        JPanel introPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel introLabel = new JLabel(
            "<html><body style='width:450px; font-family:Serif; font-size:18pt; "
            + "font-style:italic; color:blue;'>Welcome to our Ordinary "
            + "Differential Equation solver! Enter any non-stiff, first order "
            + "differential equation and its initial values, and choose from "
            + "the available numerical methods to see a graph of the solution."
            + "</body></html>");
        
        // Create instruction panel and text
        JPanel instructionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel instructionLabel = new JLabel(
            "<html><body style='width:450px; font-family:Serif; font-size:18pt; "
            + "font-style:italic; color:blue;'>Please select a previous configuration "
            + "from the drop-down menu, or enter a new set of parameters "
            + "into the required inputs text fields.</body></html>");
        
        // Create a border to separate the introduction from the input area
        JSeparator border = new JSeparator(SwingConstants.HORIZONTAL);
        border.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        
        // Create a list of hash maps containing the five most recent configurations
        List<HashMap<String, Object>> previous = getRecentEquations(5);
        
        // Create an array of strings to represent previous configurations that
        // will be added to the drop-down menu
        ArrayList<String> configs = new ArrayList();
        configs.add("-- Select a previous configuration --");
            
        // Create a string represenation of each previous configuration
        for (HashMap<String, Object> prev : previous) {
            if (prev != null) {
                String eq = prev.get("Equation").toString();
                String x0 = prev.get("x_0").toString();
                String y0 = prev.get("y_0").toString();
                String xEnd = prev.get("xEnd").toString();
                String h = prev.get("h").toString();
                String config = "dy/dx = " + eq + ", x_0 = " + x0 + ", y_0 = " 
                        + y0 + ", xEnd = " + xEnd + ", h = " + h;
                configs.add(config);
            }
        }
        
        // Create the drop down menu
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String config : configs) {
            model.addElement(config);
        }

        JComboBox<String> prevConfigs = new JComboBox<>(model);
                
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
        
        // Create panels for section headers
        JPanel prevConfigsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel requiredInputsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel extraInputsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel availMethodsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
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
        
        // Create labels for section headers 
        JLabel prevConfigsLabel = new JLabel(
            "<html><span style='font-family:SansSerif; font-size:16pt; "
            + "font-weight:bold; color:#444444;'><u>Previous Configurations:</u></span></html>");
        JLabel requiredInputsLabel = new JLabel(
            "<html><span style='font-family:SansSerif; font-size:16pt; "
            + "font-weight:bold; color:#444444;'><u>Required Inputs:</u></span></html>");
        JLabel extraInputsLabel = new JLabel(
            "<html><span style='font-family:SansSerif; font-size:16pt; "
            + "font-weight:bold; color:#444444;'><u>Additional Inputs for "
            + "Adams-Bashforth:</u></span></html>");
        JLabel availMethodsLabel = new JLabel(
            "<html><span style='font-family:SansSerif; font-size:16pt; "
            + "font-weight:bold; color:#444444;'><u>Select at least one of the "
            + "following methods:</u></span></html>");
        
        // Create text fields for each input
        JTextField equationField = new JTextField("Enter your ODE.");
        JTextField initXField = new JTextField("Enter an initial X value.");
        JTextField initYField = new JTextField("Enter an initial Y value.");
        JTextField finalXField = new JTextField("Enter a final X value.");
        JTextField stepField = new JTextField("Enter a step size.");
        JTextField nStepField = new JTextField("Enter the number of previous steps to use.");
        JTextField minStepField = new JTextField("Enter the minimum step size.");
        JTextField maxStepField = new JTextField("Enter the maximum step size.");
        
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
        
        // Add the labels, fields, and check boxes to their respective panels
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
        instructionPanel.add(instructionLabel);
        
        // Adding labels to section header panels
        prevConfigsPanel.add(prevConfigsLabel);
        requiredInputsPanel.add(requiredInputsLabel);
        extraInputsPanel.add(extraInputsLabel);
        availMethodsPanel.add(availMethodsLabel);
        
        // Add all contents to the frame
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(introPanel);
        frame.add(instructionPanel);
        frame.add(border);
        frame.add(Box.createVerticalStrut(20));
        frame.add(prevConfigsPanel);
        frame.add(prevConfigs);
        frame.add(Box.createVerticalStrut(20));
        frame.add(requiredInputsPanel);
        frame.add(equationPanel);
        frame.add(initXPanel);
        frame.add(initYPanel);
        frame.add(finalXPanel);
        frame.add(stepPanel);
        frame.add(Box.createVerticalStrut(20));                
        frame.add(extraInputsPanel);
        frame.add(nStepPanel);
        frame.add(minStepPanel);
        frame.add(maxStepPanel);
        frame.add(Box.createVerticalStrut(20));                
        frame.add(availMethodsPanel);
        frame.add(methodPanel);
        frame.add(saveButton);        
        
        // Create an ActionListener to save user inputs and populate the HashMap
        saveButton.addActionListener(event -> {
            boolean validInput = true;
            
            // Record the index selected in the drop-down menu
            int configChoice = prevConfigs.getSelectedIndex();
            
                // Determine which check boxes were selected
                eulerSelected = eulerBox.isSelected();
                rk4Selected = rk4Box.isSelected();
                midpointSelected = midBox.isSelected();
                adamBashSelected = adamBashBox.isSelected();
                
                // If the user selected a previous configuration, set the 
                // input variables based on it
                if (configChoice > 0) {
                    equation = previous.get(configChoice - 1).get("Equation").toString();
                    initX = Double.parseDouble(previous.get(configChoice - 1).get("x_0").toString());
                    initY = Double.parseDouble(previous.get(configChoice - 1).get("y_0").toString());
                    finalX = Double.parseDouble(previous.get(configChoice - 1).get("xEnd").toString());
                    step = Double.parseDouble(previous.get(configChoice - 1).get("h").toString());     
                    
                    if (adamBashSelected) {
                        try {
                            nSteps = Integer.parseInt(nStepField.getText());
                            minStep = Double.parseDouble(minStepField.getText());
                            maxStep = Double.parseDouble(maxStepField.getText());                    
                        } catch (NumberFormatException ex) {
                            validInput = false;
                            
                            JOptionPane.showMessageDialog(frame, "Please enter "
                                + "valid numerical values for the additional "
                                + "Adams-Bashforth inputs if you would like to "
                                + "use that method.");                             
                        }
                    }                    
                }
                // Otherwise, set input variables based on text fields
                else {
                    // Verify that the user inputted appropriate values and throw
                    // an exception if not                    
                    try {
                        equation = equationField.getText();
                        initX = Double.parseDouble(initXField.getText());
                        initY = Double.parseDouble(initYField.getText());
                        finalX = Double.parseDouble(finalXField.getText());
                        step = Double.parseDouble(stepField.getText());                        
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Please enter valid "
                            + "numeric values for each of the required inputs "
                            + "or select a previous configuration.");  
                        
                        validInput = false;
                    }
               
                    if (adamBashSelected) {
                        try {
                            nSteps = Integer.parseInt(nStepField.getText());
                            minStep = Double.parseDouble(minStepField.getText());
                            maxStep = Double.parseDouble(maxStepField.getText());                    
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Please enter "
                                + "valid numerical values for the additional "
                                + "Adams-Bashforth inputs if you would like to "
                                + "use that method."); 
                            
                            validInput = false;
                        }
                    }
                }
                    
                if (validInput) {
                    saved[0] = true; // Mark that inputs are saved
                    frame.dispose(); // Close the frame after saving inputs                      
                }             
        });
        
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
        
        return inputs;
    }    
    
    /**
     * Displays solution window containing the graph of each method selected
     * by the user.
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
            "x",                                                         // X-axis label (time)
            "y",                                                         // Y-axis label
            dataset                                                      // Data
        );

        // Display the chart
        ChartFrame frame = new ChartFrame("Integration Results", chart);
        frame.pack();
        frame.setVisible(true);

    }
}