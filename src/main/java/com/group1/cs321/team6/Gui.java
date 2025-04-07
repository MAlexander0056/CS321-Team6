package com.group1.cs321.team6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

/**
 * This class organizes all of the different pages that we will use to display our information.
 * It interfaces with both the Factory pattern and the SQL database for storing/retrieving equation setups.
 * Includes support for returning to the menu, enhanced navigation, and Enter key submission.
 */
public class Gui {
    
    // Instance variables for equation parameters
    private String equation = "";
    private double initX = 0.0;
    private double initY = 0.0;
    private double finalX = 0.0;
    private double step = 0.0;
    private boolean eulerSelected = false;
    private boolean rk4Selected = false;
    
    // Instance variables for user authentication
    private String username = "";
    private String password = "";
    private boolean isLoggedIn = false;
    
    // Database connection variables
    private Connection dbConnection = null;
    private final String dbUrl;
    
    /**
     * Constructor initializes database connection
     */
    public Gui() {
        // Set up database URL
        File dbDir = new File(System.getProperty("user.home"), ".EquationSolver");
        File dbFile = new File(dbDir, "StoredEquations.db");
        this.dbUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath().replace("\\", "/");
        
        // Ensure the database is initialized
        try {
            createUserTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error initializing database: " + e.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Creates the login window for user authentication
     * 
     * @return boolean indicating if login was successful
     */
    public boolean showLoginWindow() {
        // Create the login window
        JFrame loginFrame = new JFrame("ODE Solver - Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(350, 200);
        loginFrame.setLayout(new BorderLayout());
        
        // Create the main panel with GridBagLayout for more control
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(15);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(usernameLabel, gbc);
        
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);
        
        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton exitButton = new JButton("Exit");  // Added Exit button
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(exitButton);  // Added to button panel
        
        // Add panels to frame
        loginFrame.add(mainPanel, BorderLayout.CENTER);
        loginFrame.add(buttonPanel, BorderLayout.SOUTH);
        
        // Create a flag to signal when login is successful
        final boolean[] loginSuccess = {false};
        
        // Create login action to reuse for button and Enter key
        Action loginAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = usernameField.getText();
                password = new String(passwordField.getPassword());
                
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(loginFrame, "Username and password cannot be empty", 
                            "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (authenticateUser(username, password)) {
                    isLoggedIn = true;
                    loginSuccess[0] = true;
                    loginFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid username or password", 
                            "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        // Add Enter key functionality to username and password fields
        // When Enter is pressed in either field, trigger the login action
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                }
            }
        };
        
        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
        
        // Login button action
        loginButton.addActionListener(loginAction);
        
        // Register button action
        registerButton.addActionListener(e -> {
            username = usernameField.getText();
            password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Username and password cannot be empty", 
                        "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (registerUser(username, password)) {
                JOptionPane.showMessageDialog(loginFrame, "Registration successful! You can now login.", 
                        "Registration Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Registration failed. Username may already exist.", 
                        "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Exit button action
        exitButton.addActionListener(e -> {
            System.exit(0);  // Exit the application
        });
        
        // Display the frame
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
        
        // Set initial focus to username field
        usernameField.requestFocusInWindow();
        
        // Wait until login is successful
        while (!loginSuccess[0]) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
            // If frame is closed, exit the application
            if (!loginFrame.isVisible() && !loginSuccess[0]) {
                System.exit(0);
            }
        }
        
        return isLoggedIn;
    }
    
    /**
     * Window for entering ODE parameters
     * 
     * @return HashMap with parameter values
     */
    public HashMap<String, Object> CreateMainWindow() {
        // Reset values for new run
        equation = "";
        initX = 0.0;
        initY = 0.0;
        finalX = 0.0;
        step = 0.0;
        eulerSelected = false;
        rk4Selected = false;
        
        // Create the main window
        JFrame frame = new JFrame("ODE Solver - Main");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        
        // Main panel using GridBagLayout for better organization
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Create labels and fields
        JLabel equationLabel = new JLabel("Equation (dy/dx):");
        JTextField equationField = new JTextField(20);
        
        JLabel initXLabel = new JLabel("Initial X:");
        JTextField initXField = new JTextField(10);
        
        JLabel initYLabel = new JLabel("Initial Y:");
        JTextField initYField = new JTextField(10);
        
        JLabel finalXLabel = new JLabel("Final X:");
        JTextField finalXField = new JTextField(10);
        
        JLabel stepLabel = new JLabel("Step Size:");
        JTextField stepField = new JTextField(10);
        
        JLabel methodLabel = new JLabel("Integration Methods:");
        JCheckBox eulerBox = new JCheckBox("Euler");
        JCheckBox rk4Box = new JCheckBox("4th Order Runge-Kutta");
        
        // Load saved setups dropdown
        JLabel savedLabel = new JLabel("Saved Setups:");
        JComboBox<String> savedSetups = new JComboBox<>();
        loadSavedSetups(savedSetups);
        
        // Add components to the grid
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        mainPanel.add(equationLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        mainPanel.add(equationField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(initXLabel, gbc);
        
        gbc.gridx = 1;
        mainPanel.add(initXField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(initYLabel, gbc);
        
        gbc.gridx = 1;
        mainPanel.add(initYField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(finalXLabel, gbc);
        
        gbc.gridx = 1;
        mainPanel.add(finalXField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(stepLabel, gbc);
        
        gbc.gridx = 1;
        mainPanel.add(stepField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(methodLabel, gbc);
        
        gbc.gridx = 1;
        JPanel methodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        methodPanel.add(eulerBox);
        methodPanel.add(rk4Box);
        mainPanel.add(methodPanel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(savedLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        mainPanel.add(savedSetups, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton plotButton = new JButton("Plot");
        JButton saveButton = new JButton("Save Setup");
        JButton loadButton = new JButton("Load Selected");
        JButton logoutButton = new JButton("Logout");
        JButton exitButton = new JButton("Exit");  // Added Exit button
        
        buttonPanel.add(plotButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(exitButton);  // Added to button panel
        
        // Add panels to frame
        frame.setLayout(new BorderLayout());
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        
        // Status bar showing logged in user
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel userLabel = new JLabel("Logged in as: " + username);
        statusPanel.add(userLabel);
        frame.add(statusPanel, BorderLayout.NORTH);
        
        // Create a flag to signal when inputs are saved
        final boolean[] saved = {false};
        
        // Create plot action to reuse for button and Enter key
        Action plotAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get values from fields
                    equation = equationField.getText();
                    initX = Double.parseDouble(initXField.getText());
                    initY = Double.parseDouble(initYField.getText());
                    finalX = Double.parseDouble(finalXField.getText());
                    step = Double.parseDouble(stepField.getText());
                    eulerSelected = eulerBox.isSelected();
                    rk4Selected = rk4Box.isSelected();
                    
                    // Validate inputs
                    if (equation.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter an equation", 
                                "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    if (!eulerSelected && !rk4Selected) {
                        JOptionPane.showMessageDialog(frame, "Please select at least one integration method", 
                                "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    saved[0] = true;
                    frame.dispose();
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numeric values for Initial X, Y, Final X, and Step Size.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        // Add Enter key functionality to all text fields
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    plotAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                }
            }
        };
        
        // Add key listener to all text fields
        equationField.addKeyListener(enterKeyListener);
        initXField.addKeyListener(enterKeyListener);
        initYField.addKeyListener(enterKeyListener);
        finalXField.addKeyListener(enterKeyListener);
        stepField.addKeyListener(enterKeyListener);
        
        // Exit button action
        exitButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(
                    frame, 
                    "Are you sure you want to exit the application?",
                    "Confirm Exit", 
                    JOptionPane.YES_NO_OPTION);
            
            if (response == JOptionPane.YES_OPTION) {
                System.exit(0);  // Exit the application
            }
        });
        
        // Logout button action - modified to return to login screen
        logoutButton.addActionListener(e -> {
            // Confirm logout
            int response = JOptionPane.showConfirmDialog(
                    frame, 
                    "Are you sure you want to logout?",
                    "Confirm Logout", 
                    JOptionPane.YES_NO_OPTION);
            
            if (response == JOptionPane.YES_OPTION) {
                // Reset user information
                username = "";
                password = "";
                isLoggedIn = false;
                
                // Close current window and signal logout (not plot)
                frame.dispose();
                
                // Use special flag for logout
                saved[0] = true;
                equation = "LOGOUT"; // Special marker value
            }
        });
        
        // Load button action
        loadButton.addActionListener(e -> {
            String selectedSetup = (String) savedSetups.getSelectedItem();
            if (selectedSetup != null && !selectedSetup.isEmpty()) {
                HashMap<String, Object> loadedSetup = loadEquationSetup(selectedSetup);
                if (loadedSetup != null) {
                    // Populate fields with loaded data
                    equationField.setText((String) loadedSetup.get("Equation"));
                    initXField.setText(String.valueOf(loadedSetup.get("x_0")));
                    initYField.setText(String.valueOf(loadedSetup.get("y_0")));
                    finalXField.setText(String.valueOf(loadedSetup.get("xEnd")));
                    stepField.setText(String.valueOf(loadedSetup.get("h")));
                    eulerBox.setSelected((Boolean) loadedSetup.get("Euler"));
                    rk4Box.setSelected((Boolean) loadedSetup.get("RK4"));
                }
            }
        });
        
        // Save button action
        saveButton.addActionListener(e -> {
            try {
                // Get values from fields
                String eq = equationField.getText();
                double x0 = Double.parseDouble(initXField.getText());
                double y0 = Double.parseDouble(initYField.getText());
                double xEnd = Double.parseDouble(finalXField.getText());
                double h = Double.parseDouble(stepField.getText());
                boolean useEuler = eulerBox.isSelected();
                boolean useRK4 = rk4Box.isSelected();
                
                // Create setup name (based on equation and timestamp)
                String setupName = eq + " (x0=" + x0 + ", y0=" + y0 + ")";
                
                // Save to database
                saveEquationSetup(setupName, eq, x0, y0, xEnd, h, useEuler, useRK4);
                
                // Refresh the dropdown
                loadSavedSetups(savedSetups);
                
                JOptionPane.showMessageDialog(frame, "Setup saved successfully!", 
                        "Save Success", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numeric values", 
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving setup: " + ex.getMessage(), 
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Plot button action
        plotButton.addActionListener(plotAction);
        
        // Center and display the frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        // Set initial focus to equation field
        equationField.requestFocusInWindow();
        
        // Wait until the user presses plot
        while (!saved[0]) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
            // If frame is closed, exit the application
            if (!frame.isVisible() && !saved[0]) {
                System.exit(0);
            }
        }

        // Check for logout (special marker)
        if ("LOGOUT".equals(equation)) {
            return null; // Return null to signal logout
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
     * Displays the results window with charts
     * 
     * @param results The integration results
     * @param params The parameters used
     */
    public void CreateDisplayWindow(HashMap<String, List<Double>> results, HashMap<String, Object> params) {
        // This method would create a window to display additional result details
        // It could show tables of values in addition to the chart
    }
    
    /**
     * Authenticates a user against the database
     * 
     * @param username Username to check
     * @param password Password to verify
     * @return boolean indicating if authentication was successful
     */
    private boolean authenticateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password); // In real app, use password hashing!
                
                ResultSet rs = stmt.executeQuery();
                return rs.next(); // True if user found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Registers a new user in the database
     * 
     * @param username Username to register
     * @param password Password to store
     * @return boolean indicating if registration was successful
     */
    private boolean registerUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            // First check if user already exists
            String checkQuery = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    return false; // User already exists
                }
            }
            
            // Insert new user
            String insertQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, password); // In real app, use password hashing!
                
                int rowsAffected = insertStmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Creates the users table if it doesn't exist
     * 
     * @throws SQLException If database operation fails
     */
    private void createUserTable() throws SQLException {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            
            // Create users table if it doesn't exist
            String createUserTable = 
                    "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL)";
            stmt.execute(createUserTable);
            
            // Create equation_setups table if it doesn't exist
            String createSetupTable = 
                    "CREATE TABLE IF NOT EXISTS equation_setups (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT NOT NULL, " +
                    "setup_name TEXT NOT NULL, " +
                    "equation TEXT NOT NULL, " +
                    "init_x REAL NOT NULL, " +
                    "init_y REAL NOT NULL, " +
                    "final_x REAL NOT NULL, " +
                    "step_size REAL NOT NULL, " +
                    "use_euler BOOLEAN NOT NULL, " +
                    "use_rk4 BOOLEAN NOT NULL, " +
                    "saved_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (username) REFERENCES users(username))";
            stmt.execute(createSetupTable);
        }
    }
    
    /**
     * Saves an equation setup to the database
     * 
     * @param setupName Friendly name for the setup
     * @param equation The differential equation
     * @param x0 Initial x
     * @param y0 Initial y
     * @param xEnd Final x
     * @param h Step size
     * @param useEuler Whether to use Euler's method
     * @param useRK4 Whether to use RK4
     * @throws SQLException If database operation fails
     */
    private void saveEquationSetup(String setupName, String equation, double x0, double y0, 
            double xEnd, double h, boolean useEuler, boolean useRK4) throws SQLException {
        
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            String insertQuery = "INSERT INTO equation_setups " +
                    "(username, setup_name, equation, init_x, init_y, final_x, step_size, use_euler, use_rk4) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                pstmt.setString(1, username);
                pstmt.setString(2, setupName);
                pstmt.setString(3, equation);
                pstmt.setDouble(4, x0);
                pstmt.setDouble(5, y0);
                pstmt.setDouble(6, xEnd);
                pstmt.setDouble(7, h);
                pstmt.setBoolean(8, useEuler);
                pstmt.setBoolean(9, useRK4);
                
                pstmt.executeUpdate();
            }
        }
    }
    
    /**
     * Loads saved equation setups into the dropdown
     * 
     * @param comboBox The combobox to populate
     */
    private void loadSavedSetups(JComboBox<String> comboBox) {
        comboBox.removeAllItems();
        
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            String query = "SELECT setup_name FROM equation_setups WHERE username = ? ORDER BY saved_date DESC";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    comboBox.addItem(rs.getString("setup_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Loads a specific equation setup from the database
     * 
     * @param setupName The name of the setup to load
     * @return HashMap with the loaded parameters
     */
    private HashMap<String, Object> loadEquationSetup(String setupName) {
        HashMap<String, Object> setup = new HashMap<>();
        
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            String query = "SELECT * FROM equation_setups WHERE username = ? AND setup_name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, username);
                pstmt.setString(2, setupName);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    setup.put("Equation", rs.getString("equation"));
                    setup.put("x_0", rs.getDouble("init_x"));
                    setup.put("y_0", rs.getDouble("init_y"));
                    setup.put("xEnd", rs.getDouble("final_x"));
                    setup.put("h", rs.getDouble("step_size"));
                    setup.put("Euler", rs.getBoolean("use_euler"));
                    setup.put("RK4", rs.getBoolean("use_rk4"));
                    return setup;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
