package com.example.courseworkfx.exceptions;

/**
 * The `InvalidDataException` class represents a custom exception that is thrown when invalid data is detected.
 */
public class InvalidDataException extends Exception {

    /**
     * Constructs a new `InvalidDataException` with a default message indicating that the input is unacceptable.
     */
    public InvalidDataException() {
        super("Custom exception caught: Your input is unacceptable!");
    }
}
