package com.revature.app.util.exceptions;

public class ResourcePersistenceException extends RuntimeException{

    public ResourcePersistenceException(Throwable cause){
        super("The provided resource oculd not be persisted to datasource", cause);
    }

    public ResourcePersistenceException(String msg){
        super(msg);
    }

    public ResourcePersistenceException(Throwable cause, String msg){
        super(msg, cause);
    }
}
