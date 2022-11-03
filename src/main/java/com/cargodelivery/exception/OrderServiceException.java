package com.cargodelivery.exception;

public class OrderServiceException extends Throwable{

    public OrderServiceException(String message) {
        super(message);
    }

    public OrderServiceException(Throwable cause) {
        super(cause);
    }
}
