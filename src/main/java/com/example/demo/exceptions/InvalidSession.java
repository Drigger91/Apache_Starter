package com.example.demo.exceptions;

public class InvalidSession extends RuntimeException {

    public InvalidSession() {
        super("401 Unauthorized");
    }
    public InvalidSession(String message) {
        super(message);
    }

    public InvalidSession(String message, Throwable cause) {
        super(message, cause);
    }
}
