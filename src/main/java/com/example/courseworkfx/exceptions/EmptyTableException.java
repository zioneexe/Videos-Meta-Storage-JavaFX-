package com.example.courseworkfx.exceptions;

/**
 * The `EmptyTableException` class represents a custom exception that is thrown when attempting
 * to perform an operation on an empty table.
 */
public class EmptyTableException extends Exception {

    /**
     * Constructs a new `EmptyTableException` with a default message indicating that the table is empty.
     */
    public EmptyTableException() {
        super("Custom exception caught: No videos found.. The table is empty!");
    }

    /**
     * Constructs a new `EmptyTableException` with the specified error message.
     *
     * @param message The error message.
     */
    public EmptyTableException(String message) {
        super(message);
    }

    /**
     * Constructs a new `EmptyTableException` with the specified error message and cause.
     *
     * @param message The error message.
     * @param cause   The cause of the exception.
     */
    public EmptyTableException(String message, Throwable cause) {
        super(message, cause);
    }
}
