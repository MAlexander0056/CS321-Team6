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
import java.util.logging.Logger;
/**
* This class uses regex patterns to test inputted paramaters.  
*/
public class InputValidator {
    private final HashMap<String, Object> inputMap;
    private static final Logger logger = Logger.getLogger(InputValidator.class.getName());

    public InputValidator(HashMap<String, Object> inputMap) {
        this.inputMap = new HashMap<>(inputMap);
    }

    private Boolean checkValue(String key, Object field) {
        String input = String.valueOf(field);
        boolean isValid;
        switch (key) {
            case "Equation":
                isValid = Pattern.matches(".*", input); // TODO Replace with actual
                break;
            case "t0","tEnd":
                isValid = Pattern.matches(".*", input); // Any num including decimals
                break;
            default:
                return Pattern.matches(".*", input); // Any num and empty
        }
        if(!isValid){
            logger.info("Validation failed for key: %s, input: %s".formatted(key, input));
        }
        return isValid;
    }

    public Boolean checkInput() {
        for (String key : inputMap.keySet()) {
            if (!checkValue(key, inputMap.get(key))) {
                return false;
            }
        }
        return true;
    }
}

 

