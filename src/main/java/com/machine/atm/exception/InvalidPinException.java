package com.machine.atm.exception;

public class InvalidPinException extends RuntimeException {


    public InvalidPinException(String message) {
        super(message);
    }
}
