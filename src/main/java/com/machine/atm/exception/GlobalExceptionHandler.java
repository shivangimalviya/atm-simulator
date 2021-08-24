package com.machine.atm.exception;

import com.machine.atm.model.UserErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = InsufficientBalanceException.class)
    protected ResponseEntity<UserErrorResponse> handleInsufficientBalanceException(InsufficientBalanceException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(formUserErrorResponse(ex, request));
    }

    @ExceptionHandler(value = InvalidAccountException.class)
    protected ResponseEntity<UserErrorResponse> handleInvalidAccountException(InvalidAccountException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(formUserErrorResponse(ex, request));
    }

    @ExceptionHandler(value = InvalidAmountException.class)
    protected ResponseEntity<UserErrorResponse> handleInvalidAmountException(InvalidAmountException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(formUserErrorResponse(ex, request));
    }

    @ExceptionHandler(value = InvalidPinException.class)
    protected ResponseEntity<UserErrorResponse> handleInvalidPinException(InvalidPinException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(formUserErrorResponse(ex, request));
    }

    @ExceptionHandler(value = OutOfMoneyException.class)
    protected ResponseEntity<UserErrorResponse> handleOutOfMoneyException(OutOfMoneyException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(formUserErrorResponse(ex, request));
    }

    @ExceptionHandler(value = OutOfServiceException.class)
    protected ResponseEntity<UserErrorResponse> handleOutOfServiceException(OutOfServiceException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(formUserErrorResponse(ex, request));
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<UserErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(formUserErrorResponse(ex, request));
    }

    private UserErrorResponse formUserErrorResponse(Exception ex, WebRequest request) {
        ServletWebRequest req = (ServletWebRequest) request;
        return UserErrorResponse.builder().uri(req.getRequest().getRequestURI()).failureReason(ex.getMessage()).build();
    }
}
