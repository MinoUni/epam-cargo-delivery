package com.cargodelivery.exception;

public class DBException extends Throwable{

    public DBException() {}

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBException(String message) {
        super(message);
    }
}
