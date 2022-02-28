package com.revature.app.util.exceptions;

public class ResourceConflictException extends RuntimeException{
    public ResourceConflictException(String message) {
        super(message);
    }
}
