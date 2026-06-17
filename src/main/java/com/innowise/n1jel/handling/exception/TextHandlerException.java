package com.innowise.n1jel.handling.exception;

public class TextHandlerException extends Exception {
    public TextHandlerException(String message) {
        super(message);
    }

    public TextHandlerException(Throwable cause) {
        super(cause);
    }

    public TextHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

}
