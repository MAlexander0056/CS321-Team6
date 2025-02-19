/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group1.cs321.team6;
import com.group1.cs321.team6.SQLConnection;

/**
 *
 * @author cates
 */

/**
 * 
 *  Responsible for executing prepared statements, creating data structure to hold user presets, or INSERT user presets
 */
public class SQLExecution {
    /**
     * 
     *  Has a connection instance as a member variable
     */
    private SQLConnection sqlConnection;
    
    /**
     * 
     * @param username
     * @param password
     * @param param1 These are the numerical parameters
     * @param param2 
     */
    public void insertUser (String username, String password, int param1, int param2) {
        
    }
    
    /**
     *  @param username
     *  @param password
     * 
     *  Retrieves user presets from database
     */
    public void retrieveUser (String username, String password) {
        
    }
    
    public void closeConnection() {
    
}
}
