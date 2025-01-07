package com.unibuc.bookmyspace.exception;

public class DeskAlreadyBookedException extends RuntimeException {
    public DeskAlreadyBookedException(String message) {
        super(message);
    }
}
