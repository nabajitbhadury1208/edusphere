package com.cts.edusphere.exceptions.genericexceptions;

public class UnauthorizedAccessException extends RuntimeException{
    public UnauthorizedAccessException(String message){
        super(message);
    }
}
