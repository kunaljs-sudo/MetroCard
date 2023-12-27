package com.geektrust.backend.entities;

public enum UserType {
    KID(50), ADULT(200), SENIOR_CITIZEN(100), TEST(0);

    private final Integer val;

    UserType(Integer val) {
        this.val = val;
    }

    public Integer getVal(){
        return val;
    }

}
