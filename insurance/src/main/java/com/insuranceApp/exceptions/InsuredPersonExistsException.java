package com.insuranceApp.exceptions;

public class InsuredPersonExistsException extends RuntimeException {

    public InsuredPersonExistsException(String message) {
        super(message);
    }
}
