package com.skillw.pouvoir.util.read;

public class Value {

    public double value;
    public char operator;

    public Value(double value, char operator) {
        this.value = value;
        this.operator = operator;
    }

    @Override
    public String toString() {
        return String.valueOf(this.operator) + this.value;
    }
}
