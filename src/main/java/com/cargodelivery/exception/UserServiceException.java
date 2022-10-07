package com.cargodelivery.exception;

public class UserServiceException extends Throwable{

    public UserServiceException(String message) {
        super(message);
    }

    public UserServiceException(Throwable cause) {
        super(cause);
    }

    public UserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
