package com.had.teleconsulting.teleconsulting.Exception;

public class ResouseNotFoundException extends Exception{
    public ResouseNotFoundException() {
        super();
    }

    public ResouseNotFoundException(String message) {
        super(message);
    }

    public ResouseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResouseNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ResouseNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
