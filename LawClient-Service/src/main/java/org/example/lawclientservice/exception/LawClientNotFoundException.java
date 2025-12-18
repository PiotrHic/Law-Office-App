package org.example.lawclientservice.exception;

public class LawClientNotFoundException extends RuntimeException {

    public LawClientNotFoundException(String message) {
        super(message);
    }
}
