package com.geektrust.backend.exceptions;

public class StationNotFoundException extends RuntimeException {

    public StationNotFoundException() {
        super();
    }

    public StationNotFoundException(String msg) {
        super(msg);
    }
}
