package Exercises.exercise3;

public class Triangle extends Shape {
    private static final long serialVersionUID = 1L;
    private double base;
    private double height;

    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    @Override
    public double calculateArea() {
        return 0.5 * base * height;
    }

    @Override
    public double calculatePerimeter() {
        return 3 * base; 
    }
}