package com.ayusutra.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // ✅ Yeh automatically getters, setters, equals, hashCode, aur toString banayega
@NoArgsConstructor // ✅ default constructor
@AllArgsConstructor // ✅ constructor with all fields
public class LombokTest {

    private String testName;
    private int testId;
    private boolean isActive;

    public static void main(String[] args) {
        // Test 1: Using NoArgsConstructor and setters
        LombokTest obj1 = new LombokTest();
        obj1.setTestName("First Test"); // Lombok generated setter
        obj1.setTestId(101);            // Lombok generated setter
        obj1.setActive(true);           // Lombok generated setter

        System.out.println("--- Test 1 ---");
        System.out.println("Name (obj1): " + obj1.getTestName()); // Lombok generated getter
        System.out.println("ID (obj1): " + obj1.getTestId());     // Lombok generated getter
        System.out.println("Active (obj1): " + obj1.isActive());  // Lombok generated getter
        System.out.println("ToString (obj1): " + obj1.toString());// Lombok generated toString

        System.out.println("\n--- Test 2 ---");
        // Test 2: Using AllArgsConstructor
        LombokTest obj2 = new LombokTest("Second Test", 202, false);
        System.out.println("Name (obj2): " + obj2.getTestName());
        System.out.println("ID (obj2): " + obj2.getTestId());
        System.out.println("Active (obj2): " + obj2.isActive());
        System.out.println("ToString (obj2): " + obj2.toString());
    }
}