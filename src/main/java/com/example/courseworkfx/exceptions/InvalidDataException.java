package com.example.courseworkfx.exceptions;

public class InvalidDataException extends Exception {
    public InvalidDataException() {
        super("Custom exception caught: Your input is unacceptable!");
    }

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}