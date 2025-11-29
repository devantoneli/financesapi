package com.grecfinances.model;

import java.math.BigDecimal;

public class CategoriaDespesa {
    private String label;
    private BigDecimal value;
    private String color;

    // Construtor
    public CategoriaDespesa(String label, BigDecimal value, String color) {
        this.label = label;
        this.value = value;
        this.color = color;
    }

    // Getters
    public String getLabel() { return label; }
    public BigDecimal getValue() { return value; }
    public String getColor() { return color; }
}
