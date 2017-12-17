package com.pastew.plague;

public class MutableDouble {

    private double value;

    public MutableDouble() {
        value = 0;
    }

    public MutableDouble(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void add(double operand) {
        value += operand;
    }

    public void subtract(double operand) {
        value -= operand;
    }

}
