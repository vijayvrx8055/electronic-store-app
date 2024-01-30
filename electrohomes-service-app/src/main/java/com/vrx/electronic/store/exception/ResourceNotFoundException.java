package com.vrx.electronic.store.exception;

import lombok.Builder;

@Builder
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException() {
        super("Resource Not Found !!");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
