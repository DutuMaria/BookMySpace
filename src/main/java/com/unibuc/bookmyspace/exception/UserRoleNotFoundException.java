package com.unibuc.bookmyspace.exception;

public class UserRoleNotFoundException extends RuntimeException {

    public UserRoleNotFoundException() { super("User Role not found!"); }
}