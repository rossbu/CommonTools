package com.benchmark;


import com.demo.RandomMessagePrinter;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RandomMessagePrinterTest {

    @Test
    public void testRandomMessagePrinting() {
        // Redirect the System.out to capture the printed message
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Run the test for multiple iterations
        int iterations = 1000;
        int probability = 1; // 1% probability
        Random random = new Random();

        for (int i = 0; i < iterations; i++) {
            RandomMessagePrinter.printRandomMessage(random, probability);
        }

        // Restore the System.out
        System.setOut(originalOut);

        // Get the printed messages
        String printedOutput = outputStream.toString();
        String[] messages = printedOutput.split(System.lineSeparator());

        // Count the number of printed messages
        int printedCount = messages.length;

        // Calculate the expected count based on the probability and number of iterations
        int expectedCount = iterations * probability / 100;

        // Assert that the actual printed count matches the expected count within a tolerance
        int tolerance = 10; // Adjust the tolerance as needed
        assertEquals(expectedCount, printedCount, tolerance);
    }
}