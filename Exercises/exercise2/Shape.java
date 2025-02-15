package Exercises.exercise2;

abstract class Shape {
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