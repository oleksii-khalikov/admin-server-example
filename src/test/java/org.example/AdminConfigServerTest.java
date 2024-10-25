package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdminConfigServerTest {
    
    /**
     * This class tests the AdminConfigServer's calculateFactorial method.
     * The calculateFactorial method takes a number as input and calculates the factorial recursively.
     * Due to the recursion limit, it might not work for large numbers.    
     */

    @Test
    public void testCalculateFactorialWithValueZero() {

        // Initialize the object of class to be tested
        AdminConfigServer adminConfigServer = new AdminConfigServer();

        // Call the method to be tested
        int result = adminConfigServer.calculateFactorial(0);

        // Assert and verify
        assertEquals(1, result, "Factorial of zero should be 1");
    }

    @Test
    public void testCalculateFactorialWithPositiveValue() {

        // Initialize the object of class to be tested
        AdminConfigServer adminConfigServer = new AdminConfigServer();

        // Call the method to be tested
        int result = adminConfigServer.calculateFactorial(5);

        // Assert and verify
        assertEquals(120, result, "Factorial of 5 should be 120");
    }

}