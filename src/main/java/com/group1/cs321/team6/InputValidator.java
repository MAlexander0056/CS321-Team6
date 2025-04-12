/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Michael A
 */
package com.group1.cs321.team6;

import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class InputValidator {
    private final HashMap<String, Object> inputMap;

    public InputValidator(HashMap<String, Object> inputMap) {
        this.inputMap = new HashMap<>(inputMap);
    }

    /*
      This checks each individual input against pre-set regex patterns.
      Returns true if the value matches at least one pattern.
     */
    private Boolean checkValue(String key,Object field) {
                
        String input = String.valueOf(field);

        final String[] patterns = {
            "quick brown",  // Matches "quick brown"
            "\\d+",         // Matches digits
            "[a-z]+",       // Matches lowercase letters
            "^$"            // Matches empty strings (optional)
        };

        for (String patternString : patterns) {
            Matcher matcher = Pattern.compile(patternString).matcher(input);
            if (matcher.find()) {
                return true; 
            }
        }
        return false; 
    }

    /**
     * Iterates through map and returns false if any values fail.
     * Passes only if ALL values pass validation.
     */
    public Boolean checkInput() {
        for (Object input : inputMap.values()) {
            if (!checkValue(input)) {
                return false; 
            }
        }
        return true;
    }
}
 

