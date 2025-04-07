package com.group1.cs321.team6;
import static com.group1.cs321.team6.CreateDatabase.initDB;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Main application class that coordinates the workflow of the ODE Solver
 * with enhanced login/logout flow and improved navigation
 * @author Admin
 */
public class CS321Team6 {

    public static void main(String[] args) {
        // Initialize the database
        initDB();
        
        // Create GUI instance
        Gui gui = new Gui();
        
        // Main application loop - keeps running until explicit exit
        boolean continueRunning = true;
        while (continueRunning) {
            // Show login screen and continue only if login is successful
            boolean loginSuccess = gui.showLoginWindow();
            if (!loginSuccess) {
                // User closed the login window or exited
                System.exit(0);
            }
            
            // User is now logged in, start the equation solving loop
            boolean stayLoggedIn = true;
            while (stayLoggedIn) {
                // Open the main window to get user inputs
                HashMap<String, Object> presets = gui.CreateMainWindow();
                
            // Check if user chose to logout (will return to login screen)
            if (presets == null) {
                System.out.println("User logged out. Returning to login screen.");
                stayLoggedIn = false;
                continue;
            }
                
                // Create UserInput object with the presets
                UserInput user = new UserInput(presets, "Cates", "password");
                
                // Use the Factory pattern to create integrators
                Factory factory = new Factory(presets, user);
                
                // Create a list of integrators based on user selection
                List<String> integrators_to_create = new ArrayList<>();
                
                // Add selected integration methods
                if ((Boolean) presets.get("Euler")) {
                    integrators_to_create.add("euler");
                }
                if ((Boolean) presets.get("RK4")) {
                    integrators_to_create.add("rk4");
                }
                
                // Run the integration
                IntegrationRunner integration_runner = new IntegrationRunner(factory);
                HashMap<String, List<Double>> result = integration_runner.performIntegration(integrators_to_create);
                
                // Print results to console (for debugging)
                for (String key : result.keySet()) {
                    List<Double> value = result.get(key);
                    System.out.println("Key: " + key + ", Value size: " + value.size());
                }
                
                // Plot the results with return to menu button
                boolean returnToMenu = plotResultsWithMenuOption(result, presets);
                
                // If user chose to exit instead of returning to menu, exit the application
                if (!returnToMenu) {
                    continueRunning = false;
                    stayLoggedIn = false;
                }
            }
        }
        
        // Exit application
        System.exit(0);
    }
    
    /**
     * Creates and displays a chart of the integration results with a Return to Menu button
     * 
     * @param result The integration results from different methods
     * @param presets The parameters used for integration
     * @return boolean indicating if user wants to return to menu (true) or exit (false)
     */
    private static boolean plotResultsWithMenuOption(HashMap<String, List<Double>> result, HashMap<String, Object> presets) {
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

        // Create chart panel
        ChartPanel chartPanel = new ChartPanel(chart);
        
        // Create button panel with Return to Menu and Exit buttons
        JPanel buttonPanel = new JPanel();
        JButton returnButton = new JButton("Return to Menu");
        JButton exitButton = new JButton("Exit Application");
        buttonPanel.add(returnButton);
        buttonPanel.add(exitButton);
        
        // Create the frame
        JFrame frame = new JFrame("Integration Results");
        frame.setLayout(new BorderLayout());
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Use an array to hold the return value since we need to access it from ActionListener
        final boolean[] returnToMenu = {false};
        
        // Add button action listeners
        returnButton.addActionListener(e -> {
            returnToMenu[0] = true;
            frame.dispose();
        });
        
        exitButton.addActionListener(e -> {
            returnToMenu[0] = false;
            frame.dispose();
        });
        
        // Handle window close as "Exit Application"
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                returnToMenu[0] = false;
            }
        });
        
        // Display the frame and wait for it to close
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        // Wait for the frame to close
        while (frame.isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        
        // Return the user's choice
        return returnToMenu[0];
    }
}
