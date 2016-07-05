package com.example.java8;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Dish {
    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;
    private Set<SideOrder> sideOrders = new HashSet<>();

    public enum Type { MEAT, FISH, DELICIOUS_VEG }

    public Dish(String name, boolean vegetarian, int calories, Type type, SideOrder... sideOrders) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
        if (sideOrders != null) {
            this.sideOrders.addAll(Arrays.asList(sideOrders));
        }
    }

    public String getName() {
        return name;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public int getCalories() {
        return calories;
    }

    public Type getType() {
        return type;
    }

    public Set<SideOrder> getSideOrders() {
        return sideOrders;
    }
}
