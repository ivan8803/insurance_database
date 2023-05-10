package com.insuranceApp.exceptions;

public class InsuredPersonNotFoundException extends RuntimeException {

    public InsuredPersonNotFoundException(String message) {
        super(message);
    }
}
