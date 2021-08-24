package com.machine.atm.exception;

public class OutOfServiceException extends RuntimeException {


    public OutOfServiceException(String message) {
        super(message);
    }

}
