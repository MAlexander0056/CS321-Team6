package com.group1.cs321.team6;
// CHANGE THIS - Specify which libray and include only what is needed
import javax.swing.*;
import java.awt.FlowLayout;
import java.util.HashMap;

/**
 * @author TBD
 */


 /**
  * This class will organize all of the different pages that we will use to display our information.
  * The goal is to have as many reusable components as possible when working with the GUI for code that is easy to maintain and update. 
  */
public class Gui {
    
    private String equation = "";
    private double initX = 0.0;
    private double initY = 0.0;
    private double finalX = 0.0;
    private double step = 0.0;
    private boolean eulerSelected = false;
    private boolean rk4Selected = false;

    /**
     * Window for introduction page. Requests information needed for 
     * 
     * @return
     */
    public HashMap<String, Object> CreateMainWindow(){
        // Create the main window
        JFrame frame = new JFrame("ODE Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        
        // Create panels for each input
        JPanel equationPanel = new JPanel(new FlowLayout());
        JPanel initXPanel = new JPanel(new FlowLayout());
        JPanel initYPanel = new JPanel(new FlowLayout());
        JPanel finalXPanel = new JPanel(new FlowLayout());
        JPanel stepPanel = new JPanel(new FlowLayout());
        JPanel methodPanel = new JPanel(new FlowLayout());
        
        // Create labels for each input
        JLabel equationLabel = new JLabel("Equation: ");
        JLabel initXLabel = new JLabel("Initial X: ");
        JLabel initYLabel = new JLabel("Initial Y: ");
        JLabel finalXLabel = new JLabel("Final X: ");
        JLabel stepLabel = new JLabel("Step Size: ");
        JLabel methodLabel = new JLabel("Methods: ");
        
        // Create text fields for each input
        JTextField equationField = new JTextField("Enter your ODE.");
        JTextField initXField = new JTextField("Enter an initial X value.");
        JTextField initYField = new JTextField("Enter an initial Y value.");
        JTextField finalXField = new JTextField("Enter a final X value.");
        JTextField stepField = new JTextField("Enter a step size.");
        
        // Create check boxes for each solver option
        JCheckBox eulerBox = new JCheckBox("Euler");
        JCheckBox rk4Box = new JCheckBox("4th Order Runge-Kutta");
        
        // Create a button to save user inputs
        JButton saveButton = new JButton("Save & Plot");    

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
        methodPanel.add(methodLabel);
        methodPanel.add(eulerBox);
        methodPanel.add(rk4Box);
        
        
        // Create an ActionListener to save user inputs and populate the HashMap
        saveButton.addActionListener(event -> {
                try {
                    equation = equationField.getText();
                    initX = Double.parseDouble(initXField.getText());
                    initY = Double.parseDouble(initYField.getText());
                    finalX = Double.parseDouble(finalXField.getText());
                    step = Double.parseDouble(stepField.getText());

                    saved[0] = true; // Mark that inputs are saved
                    frame.dispose(); // Close the frame after saving inputs
                

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid "
                            + "numeric values for Initial X, Y, Final X, and "
                            + "Step Size.");
                }
        });
        
        // Create an ActionListener to save which solver methods are selected
        if (eulerBox.isSelected()){
            eulerSelected = true;
        }
        if (rk4Box.isSelected()){
            rk4Selected = true;
        }
        
        // Add the panels and button to the window
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(equationPanel);
        frame.add(initXPanel);
        frame.add(initYPanel);
        frame.add(finalXPanel);
        frame.add(stepPanel);
        frame.add(methodPanel);
        frame.add(saveButton);
        
        frame.setVisible(true);
                
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
        inputs.put("Euler", eulerSelected);
        inputs.put("RK4", rk4Selected);

        return inputs;
    }    
    
    /**
     * Window will display processed information
     */
    void CreateDisplayWindow(){

    }
}