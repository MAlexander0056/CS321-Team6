/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group1.cs321.team6;
import com.group1.cs321.team6.SQLExecution;
import java.util.HashMap;
/**
 *
 * @author cates
 */

/**
 * 
 *  Handles user input, packages and prepares for database INSERT
 *  A user consists of username, password (may not need), and their numerical inputs
 */
public class UserInput {
    /** 
     * 
     *  Presets will be something of the form: {"Equation"=value1, "Initial Condition"=value, "Other Parameter1=null", etc...}
     *     
     */
    // Potential advantage of HashMaps: They allow for null values, which should prove more useful than maps.
    
    
    private HashMap<String, Object> presets;
    
    UserInput (HashMap<String, Object> presetsInput, String usernameInput, String passwordInput) {
        this.presets = presetsInput;
        this.username = usernameInput;
        this.password = passwordInput;
    }
    
    /**
     * 
     *  These getters are neccessary for other classes to perform database retrieval and insertion 
     */
    public String getUsername () {
        return this.username;
    }
    
    public String getPassword () {
        return this.password;
    }
    
    public HashMap<String, Object> getPresets() {
        return this.presets;
    }
    
    /**
     * 
     *  @return 
     */
    private String handleInput () {
        return null;
    }
}
