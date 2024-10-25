package org.example;

import org.junit.jupiter.api.Test;

public class ExampleTest {

    @Test
    void demo() {

        int result = calculateFactorial(200);
        System.out.println(result);

    }

    private int calculateFactorial(int value) {
        if (value == 0) {
            return 1;
        }
        else {
            return value * calculateFactorial(value - 1);
        }
    }
}