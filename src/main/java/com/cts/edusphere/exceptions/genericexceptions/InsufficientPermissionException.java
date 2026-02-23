package com.cts.edusphere.exceptions.genericexceptions;

public class InsufficientPermissionException extends RuntimeException{
    public InsufficientPermissionException(String message){
        super(message);
    }
}
