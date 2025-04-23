package Exercises.exercise4;

import java.util.regex.*;

public class RegexDemo {
    public static void main(String[] args) {
        testRegex("Email Pattern", 
            "^[\\w.-]+@[\\w.-]+\\.\\w{2,}$", 
            "john.doe@example.com", 
            "invalid-email@");

        testRegex("Phone Number (US)", 
            "^\\(\\d{3}\\) \\d{3}-\\d{4}$", 
            "(123) 456-7890", 
            "123-456-7890");

        testRegex("Only Letters", 
            "^[A-Za-z]+$", 
            "JustLetters", 
            "Letters123");

        testRegex("Hex Color Code", 
            "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", 
            "#1f2E3d", 
            "#1234");

        testRegex("Password Strength (min 8, upper, lower, digit)",
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
            "Str0ngPass",
            "weakpass");
    }

    public static void testRegex(String label, String patternStr, String positiveCase, String negativeCase) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher1 = pattern.matcher(positiveCase);
        Matcher matcher2 = pattern.matcher(negativeCase);

        System.out.println("=== " + label + " ===");
        System.out.println("Pattern: " + patternStr);

        System.out.println("Positive Case [" + positiveCase + "] -> " + matcher1.matches());
        System.out.println("Negative Case [" + negativeCase + "] -> " + matcher2.matches());
        System.out.println();
    }
}
