package com.innowise.n1jel.handling.exception;

public class TextCustomException extends Exception {

    public TextCustomException(String message) {
        super(message);
    }

    public TextCustomException(Throwable cause) {
        super(cause);
    }

    public TextCustomException(String message, Throwable cause) {
        super(message, cause);
    }

}
