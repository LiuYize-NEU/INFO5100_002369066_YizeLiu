// 文件路径：Exercises/exercise5/DesignPatternDemo.java
package Exercises.exercise5;

import java.util.*;

// 1. Singleton Pattern - System Logger
class Logger {
    private static Logger instance;

    private Logger() {}

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String message) {
        System.out.println("[LOG]: " + message);
    }
}

// 2. Factory Pattern - Product Delivery
interface Product {
    void deliver();
}

class Book implements Product {
    public void deliver() {
        System.out.println("Delivering a book by postal service.");
    }
}

class Toy implements Product {
    public void deliver() {
        System.out.println("Delivering a toy by courier.");
    }
}

class ProductFactory {
    public static Product createProduct(String type) {
        if (type.equalsIgnoreCase("book")) {
            return new Book();
        } else if (type.equalsIgnoreCase("toy")) {
            return new Toy();
        } else {
            throw new IllegalArgumentException("Unknown product type");
        }
    }
}

// 3. Observer Pattern - Price Notification
interface Observer {
    void update(double price);
}

interface Subject {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}

class ProductPrice implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private double price;

    public void setPrice(double price) {
        this.price = price;
        notifyObservers();
    }

    public void registerObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(price);
        }
    }
}

class Customer implements Observer {
    private String name;

    public Customer(String name) {
        this.name = name;
    }

    public void update(double price) {
        System.out.println(name + " received new price update: $" + price);
    }
}

public class DesignPatternDemo {
    public static void main(String[] args) {
        System.out.println("--- Singleton Pattern ---");
        Logger logger = Logger.getInstance();
        logger.log("Application started");

        System.out.println("\n--- Factory Pattern ---");
        Product p1 = ProductFactory.createProduct("book");
        Product p2 = ProductFactory.createProduct("toy");
        p1.deliver();
        p2.deliver();

        System.out.println("\n--- Observer Pattern ---");
        ProductPrice productPrice = new ProductPrice();
        Customer alice = new Customer("Alice");
        Customer bob = new Customer("Bob");

        productPrice.registerObserver(alice);
        productPrice.registerObserver(bob);

        productPrice.setPrice(29.99);
        productPrice.setPrice(19.99);
    }
}
