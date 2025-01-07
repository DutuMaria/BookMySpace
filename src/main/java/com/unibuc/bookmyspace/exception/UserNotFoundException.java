package com.unibuc.bookmyspace.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() { super("User not found!"); }
}