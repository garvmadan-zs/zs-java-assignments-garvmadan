package com.zs.assignment1112.exception;


/**
 * Exception thrown when requested data is not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
