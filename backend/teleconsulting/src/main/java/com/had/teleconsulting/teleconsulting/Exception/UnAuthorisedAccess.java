package com.had.teleconsulting.teleconsulting.Exception;

public class UnAuthorisedAccess extends Exception{
    public UnAuthorisedAccess() {
        super();
    }

    public UnAuthorisedAccess(String message) {
        super(message);
    }

    public UnAuthorisedAccess(String message, Throwable cause) {
        super(message, cause);
    }

    public UnAuthorisedAccess(Throwable cause) {
        super(cause);
    }

    protected UnAuthorisedAccess(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
