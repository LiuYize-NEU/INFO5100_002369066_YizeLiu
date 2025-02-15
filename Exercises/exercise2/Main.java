package Exercises.exercise2;

public class Main {
    public static void main(String[] args) {
        Shape.setColor("Red");

        Shape triangle = new Triangle(9, 10);
        Shape rectangle = new Rectangle(9, 6);
        Shape circle = new Circle(9);
        Shape square = new Square(10);

        triangle.displayClassName();
        System.out.println("Area: " + triangle.calculateArea());
        System.out.println("Perimeter: " + triangle.calculatePerimeter());
        System.out.println("Color: " + Shape.getColor());

        System.out.println();

        rectangle.displayClassName();
        System.out.println("Area: " + rectangle.calculateArea());
        System.out.println("Perimeter: " + rectangle.calculatePerimeter());
        System.out.println("Color: " + Shape.getColor());

        System.out.println();

        circle.displayClassName();
        System.out.println("Area: " + circle.calculateArea());
        System.out.println("Perimeter: " + circle.calculatePerimeter());
        System.out.println("Color: " + Shape.getColor());

        System.out.println();

        square.displayClassName();
        System.out.println("Area: " + square.calculateArea());
        System.out.println("Perimeter: " + square.calculatePerimeter());
        System.out.println("Color: " + Shape.getColor());
    }
}
