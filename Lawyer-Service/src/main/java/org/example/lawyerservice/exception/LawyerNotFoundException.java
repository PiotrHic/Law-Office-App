package org.example.lawyerservice.exception;

public class LawyerNotFoundException extends RuntimeException {

    public LawyerNotFoundException (String message) {
        super(message);
    }
}
