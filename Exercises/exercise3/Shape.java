package Exercises.exercise3;

import java.io.Serializable;

public abstract class Shape implements Serializable {
    private static final long serialVersionUID = 1L;
    protected static String color = "Unknown";

    public abstract double calculateArea();
    public abstract double calculatePerimeter();

    public static String getColor() {
        return color;
    }

    public static void setColor(String newColor) {
        color = newColor;
    }

    public void displayClassName() {
        System.out.println("Class name: " + this.getClass().getSimpleName());
    }
}