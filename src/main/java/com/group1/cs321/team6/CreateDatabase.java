package com.group1.cs321.team6;

import static com.group1.cs321.team6.DbConfig.JDBC_URL;
import static com.group1.cs321.team6.DbConfig.DB_PATH;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDatabase {

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
                        "equation TEXT NOT NULL, " +
                        "result TEXT, " +
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
