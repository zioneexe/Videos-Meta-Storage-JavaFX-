package com.example.courseworkfx.exceptions;

public class EmptyTableException extends Exception {
    public EmptyTableException() {
        super("Custom exception caught: No videos found.. The database is empty!");
    }

    public EmptyTableException(String message) {
        super(message);
    }

    public EmptyTableException(String message, Throwable cause) {
        super(message, cause);
    }
}
