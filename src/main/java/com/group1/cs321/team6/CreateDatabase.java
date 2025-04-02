package com.group1.cs321.team6;
        
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDatabase {

    public static void initDB() {
        try {
            // Creating folder if doesn't exist
            File dbDir = new File(System.getProperty("user.home"), ".EquationSolver");
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }

            // jdbc needs \ and not / it will break if replace isn't there
            File dbFile = new File(dbDir, "StoredEquations.db");
            String jdbcUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath().replace("\\", "/");

            // Create and/or connect to the database
            try (Connection conn = DriverManager.getConnection(jdbcUrl)) {
                System.out.println("Database connection established successfully");

                // Create tables if they don't exist
                try (Statement stmt = conn.createStatement()) {
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
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }
}
