package com.codegym.exception;

import com.codegym.utils.StatusCode;

public class ResourceNotFoundException extends AppException {

    public ResourceNotFoundException(StatusCode statusCode, Object... args) {
        super(statusCode, args);
    }
}