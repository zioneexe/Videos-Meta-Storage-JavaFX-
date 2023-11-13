package com.example.courseworkfx.exceptions;

/**
 * The `InvalidFileFormatException` class represents a custom exception that is thrown when an invalid file format is detected.
 */
public class InvalidFileFormatException extends Exception {

    /**
     * Constructs a new `InvalidFileFormatException` with a default message indicating that the file format is incorrect.
     */
    public InvalidFileFormatException() {
        super("Custom exception caught: The file format is incorrect. Search for .txt");
    }

    /**
     * Constructs a new `InvalidFileFormatException` with the specified error message.
     *
     * @param message The error message.
     */
    public InvalidFileFormatException(String message) {
        super(message);
    }

    /**
     * Constructs a new `InvalidFileFormatException` with the specified error message and cause.
     *
     * @param message The error message.
     * @param cause   The cause of the exception.
     */
    public InvalidFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
