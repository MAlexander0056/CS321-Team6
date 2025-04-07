/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group1.cs321.team6;
import java.util.HashMap;
import static com.group1.cs321.team6.InputValidator.checkIfValid;


/**
 *
 * @author Michael A
 */

/**
 * 
 *  Responsible for adding legitimate values to the Database
 */

public class AddToDB {
    private final HashMap<String, Object> inputMap;
    
    public AddToDB(HashMap<String, Object> inputMap) {
        this.inputMap = new HashMap<>(inputMap); 
    }
    
    public Boolean saveToDatabase() {
        Boolean pass = false;
        for(Object input : inputMap.values()){
            if(checkIfValid(input) == false){
                pass = false;
            }
            if(pass){
                // TODO add logic try with resources to add all vals to row
            }
        }
        return pass;
    }
}

