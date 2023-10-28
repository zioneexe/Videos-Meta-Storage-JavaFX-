package com.example.courseworkfx.exceptions;

public class InvalidFilenameException extends Exception {
    public InvalidFilenameException() {
        super("Custom exception caught: The filename is incorrect. Try another.");
    }

    public InvalidFilenameException(String message) {
        super(message);
    }

    public InvalidFilenameException(String message, Throwable cause) {
        super(message, cause);
    }
}