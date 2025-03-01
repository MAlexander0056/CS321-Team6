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
           
    @Override
    public void execute() throws MojoExecutionException {
        
        // Creating folder if doesnt exist
        File dbDir = new File(System.getProperty("user.home"), ".EquationSolver");
        if (!dbDir.exists()) {
            dbDir.mkdirs();
        }
        
        // jdbc needs \ and not / it will break if replace isnt there
        File dbFile = new File(dbDir, "StoredEquations.db");
        String jdbcUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath().replace("\\", "/");

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
                }
            }
        } catch (SQLException e) {
            throw new MojoExecutionException("Failed to initialize database: " + e.getMessage(), e);
        }
        
    }
}









