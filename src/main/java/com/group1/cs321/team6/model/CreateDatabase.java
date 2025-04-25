package com.group1.cs321.team6.model;

import static com.group1.cs321.team6.model.DbConfig.JDBC_URL;
import static com.group1.cs321.team6.model.DbConfig.DB_PATH;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

    /**
    * This class should not be initialized however it holds the logic to create 
    * the database.
    */
public class CreateDatabase {    
    
    /**
    * Default constructor for CreateDatabase.
    */
    public CreateDatabase() {
        // No initialization required
    }
    
    /**
     *This function initializes the database with the correct tables. It will create
     *a folder in the users home directory if not present and create the db file there.
     *This should always be run at the start of the program.
     */
    public static void initDB() {

        // jdbc needs \ and not / it will break if replace isn't there
        File dbFile = new File(DB_PATH, "StoredEquations.db");
        
        try {

            // Create and/or connect to the database
            try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
                System.out.println("Database connection established successfully");

                // Create tables if they don't exist
                try (Statement stmt = conn.createStatement()) {
                    String createTableSQL =
                        "CREATE TABLE IF NOT EXISTS Equations (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "Equation TEXT, " +
                        "x_0 REAL, " +
                        "y_0 REAL, " +
                        "xEnd, " +
                        "nSteps INTEGER, " +
                        "minStep REAL, " +
                        "maxStep REAL, " +
                        "h REAL, " +
                        "Euler INTEGER, " +
                        "RK4 INTEGER, " +
                        "Midpoint REAL, " +
                        "Adam_Bashforth INTEGER, " +
                        "saved_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")";
                    stmt.execute(createTableSQL);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }
}
