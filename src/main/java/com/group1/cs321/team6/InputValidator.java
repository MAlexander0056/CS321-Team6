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

    private Boolean checkValue(String key, Object field) {
        String input = String.valueOf(field);
            
        switch (key) {
            case "equation":
                return Pattern.matches(".*", input); // TODO Replace with actual
            case "t0","tEnd":
                return Pattern.matches("^-?\\d*\\.?\\d+$", input); // Any num including decimals
            default:
                return Pattern.matches("^-?\\d*\\.?\\d*$|^$", input); // Any num and empty
        }
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

 

