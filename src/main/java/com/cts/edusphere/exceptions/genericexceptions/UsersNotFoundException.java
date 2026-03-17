package com.cts.edusphere.exceptions.genericexceptions;

public class UsersNotFoundException extends RuntimeException {
    public UsersNotFoundException(String mssg) {
        super(mssg);
    }
}
