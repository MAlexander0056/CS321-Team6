/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group1.cs321.team6;

import java.io.File;

/**
 * This class holds the commonly used paths to important database related locations.
 */
public class DbConfig {
    /**
     * Default constructor for DbConfig
     */
    public DbConfig(){
        // Explicit constructor for documentation
    }
    /**
     *Variable that holds the path to the database. 
     */
    public static final String DB_PATH = System.getProperty("user.home") + "/.EquationSolver/StoredEquations.db";
    /**
     *Variable that holds the needed path for connection to the database 
     */
    public static final String JDBC_URL = "jdbc:sqlite:" + DB_PATH.replace("\\", "/");

    static {
        new File(DB_PATH).getParentFile().mkdirs();  // Ensure directory exists
    }
}
