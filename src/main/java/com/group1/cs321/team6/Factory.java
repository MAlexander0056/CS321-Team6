/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group1.cs321.team6;
import com.group1.cs321.team6.EulersMethod;
import com.group1.cs321.team6.SQLExecution;
import com.group1.cs321.team6.UserInput;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author cates, Michael
 */

/**
 * 
 *  Compares retrieved SQLExecution data to the list of parameters needed to execute each solve.
 *  Then, creates solver objects, executing each solve.
 */
public class Factory {
    private HashMap<String, Integer> givenParameters;
    private UserInput userToRetrieve;
    
    Factory (HashMap<String, Integer> retrievedUserPresets, UserInput someUser) {
        this.userToRetrieve = someUser;
        this.givenParameters = retrievedUserPresets;
    }
    
    /**
     * 
     * @return returns a list of solver instances
     */
    private List<Object> createSolvers () {
        return null;
    }
    
}
