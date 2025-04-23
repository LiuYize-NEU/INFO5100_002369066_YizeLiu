package Exercises.exercise3;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {
            Shape.setColor("Red");

            Shape[] shapes = {
                new Triangle(9, 10),
                new Rectangle(9, 6),
                new Circle(9),
                new Square(10)
            };

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("shapes.ser"));
            oos.writeObject(shapes);
            oos.close();
            System.out.println("Shapes serialized to shapes.ser\n");

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("shapes.ser"));
            Shape[] loadedShapes = (Shape[]) ois.readObject();
            ois.close();

            for (Shape shape : loadedShapes) {
                shape.displayClassName();
                System.out.println("Area: " + shape.calculateArea());
                System.out.println("Perimeter: " + shape.calculatePerimeter());
                System.out.println("Color: " + Shape.getColor());
                System.out.println();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
