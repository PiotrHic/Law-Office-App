package org.example.lawcaseservice.exception;

public class LawCaseNotFoundException extends RuntimeException {

    public LawCaseNotFoundException(String message) {
        super(message);
    }
}
