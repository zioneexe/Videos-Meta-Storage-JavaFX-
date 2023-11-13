package com.example.courseworkfx;

import javafx.util.StringConverter;

/**
 * A custom StringConverter for converting between Double and String with exception handling.
 */
public class DoubleConverter extends StringConverter<Double> {

    /**
     * Converts a Double to a String.
     *
     * @param object The Double to be converted.
     * @return The String representation of the Double, or an empty string if the Double is null.
     */
    @Override
    public String toString(Double object) {
        // Convert Double to String
        return object == null ? "" : object.toString();
    }

    /**
     * Converts a String to a Double, handling NumberFormatException.
     *
     * @param string The String to be converted to a Double.
     * @return The converted Double, or null if the String cannot be parsed as a valid number.
     */
    @Override
    public Double fromString(String string) {
        // Convert String to Double, handle exception
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            // Handle the exception by showing an alert or logging
            System.out.println("Invalid input. Must be a valid number.");
            return null; // or throw your custom exception if needed
        }
    }
}
