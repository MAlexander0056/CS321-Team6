/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.group1.cs321.team6;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author Michael A
 */
public class InputValidator {
    
    public static Boolean checkIfValid(Object field){
        Boolean pass = false;
    //    TODO swap with actual restrictions        
        final String[] patterns = {
                "quick brown", // Matches the phrase "quick brown"
                "\\d+",         // Matches one or more digits
                "[a-z]+"        // Matches one or more lowercase letters
        };
        
        for(String patternString : patterns){
         Pattern pattern = Pattern.compile(patternString);
         Matcher matcher = pattern.matcher((String)field);
         
         pass = matcher.find();
        }
        return pass;
      
        
    };
}
