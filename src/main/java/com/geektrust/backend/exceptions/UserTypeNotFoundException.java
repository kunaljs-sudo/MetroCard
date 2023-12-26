package com.geektrust.backend.exceptions;

public class UserTypeNotFoundException extends RuntimeException {
    public UserTypeNotFoundException() {
        super();
    }

    public UserTypeNotFoundException(String msg) {
        super(msg);
    }
}
