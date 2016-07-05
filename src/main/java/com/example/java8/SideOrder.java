package com.example.java8;

public class SideOrder {
    public enum Type { ROAST_POTATOES, CHIPS, VEGETABLES, SALAD }

    private final int calories;
    private final Type type;

    public SideOrder(Type type, int calories) {
        this.type = type;
        this.calories = calories;
    }

    public int getCalories() {
        return calories;
    }

    public Type getType() {
        return type;
    }
}
