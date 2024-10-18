package com.ecommerce.exception;

import lombok.Builder;

@Builder
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(){
        super("Resource not Found !!");
    }

    public ResourceNotFoundException(String message){
        super(message);
    }


}
