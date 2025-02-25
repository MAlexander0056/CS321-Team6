package com.group1.plugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Maven plugin to initialize the SQLite database for equation storage.
 */
@Mojo(name = "initialize-database", defaultPhase = LifecyclePhase.INITIALIZE)
public class CreateDatabase extends AbstractMojo {

    /**
     * Path to the database file.
     */
    @Parameter(defaultValue = "${user.home}/.EquationSolver/StoredEquations.db", 
               property = "dbPath", required = true)
    private String dbPath;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Initializing database at: " + dbPath);
        
        // Extract directory path from the full DB path
        File directory = new File(System.getProperty("user.home") + "/.EquationSolver");
        
        // Create directory if it doesn't exist
        if (!directory.exists()) {
            getLog().info("Creating directory: " + directory.getAbsolutePath());
            if (!directory.mkdirs()) {
                throw new MojoExecutionException("Failed to create directory: " + directory.getAbsolutePath());
            }
            getLog().info("Directory created successfully");
        } else {
            getLog().info("Directory already exists: " + directory.getAbsolutePath());
        }
        
        // Construct the JDBC URL
        String jdbcUrl = "jdbc:sqlite:" + dbPath;
        getLog().info("JDBC URL: " + jdbcUrl);
        
        try {
            // Create and/or connect to the database
            try (Connection conn = DriverManager.getConnection(jdbcUrl)) {
                getLog().info("Database connection established successfully");
                
                // Create tables if they don't exist
                try (Statement stmt = conn.createStatement()) {
                    // Example: Create a simple table for storing equations
                    String createTableSQL = 
                        "CREATE TABLE IF NOT EXISTS equations (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "equation TEXT NOT NULL, " +
                        "result TEXT, " + 
                        "saved_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")";
                    
                    stmt.execute(createTableSQL);
                    getLog().info("Database tables initialized successfully");
                }
            }
        } catch (SQLException e) {
            throw new MojoExecutionException("Failed to initialize database: " + e.getMessage(), e);
        }
        
        getLog().info("Database initialization completed successfully");
    }
}