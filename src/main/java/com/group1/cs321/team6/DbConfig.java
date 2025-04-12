/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group1.cs321.team6;

import java.io.File;

/**
 *
 * @author Admin
 */

// Configuration class
public class DbConfig {
    public static final String DB_PATH = System.getProperty("user.home") + "/.EquationSolver/StoredEquations.db";
    public static final String JDBC_URL = "jdbc:sqlite:" + DB_PATH.replace("\\", "/");

    static {
        new File(DB_PATH).getParentFile().mkdirs();  // Ensure directory exists
    }
}
