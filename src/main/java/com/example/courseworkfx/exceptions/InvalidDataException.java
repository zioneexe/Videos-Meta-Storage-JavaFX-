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

    /**
     * Constructs a new `InvalidDataException` with the specified error message.
     *
     * @param message The error message.
     */
    public InvalidDataException(String message) {
        super(message);
    }

    /**
     * Constructs a new `InvalidDataException` with the specified error message and cause.
     *
     * @param message The error message.
     * @param cause   The cause of the exception.
     */
    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
