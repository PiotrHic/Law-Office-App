package org.example.exception;

public class LawClientNotFoundException extends RuntimeException {

    public LawClientNotFoundException(String message) {
        super(message);
    }
}
