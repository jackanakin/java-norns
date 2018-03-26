package com.ark.norns.exception;

public class PwdException extends RuntimeException {
    public String message;
    public Throwable cause;

    public PwdException() {
        message = "Mib file path must be defined";
        cause = new Throwable("Path to mib file is not defined");
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
