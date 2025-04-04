package com.group1.cs321.team6;

import java.util.List;

public interface ODESolver {
    void integrate();           // Performs the integration
    List<Double> getYValues();  // Retrieves the y-values after integration
}