package com.had.teleconsulting.teleconsulting.Exception;

public class PatientNotFoundExeption extends Exception{
    public PatientNotFoundExeption() {
        super();
    }

    public PatientNotFoundExeption(String message) {
        super(message);
    }

    public PatientNotFoundExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public PatientNotFoundExeption(Throwable cause) {
        super(cause);
    }

    protected PatientNotFoundExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
