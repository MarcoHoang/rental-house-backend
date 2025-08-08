package com.codegym.exception;

import com.codegym.utils.StatusCode;

public class DuplicateUsernameException extends AppException {
    public DuplicateUsernameException() {
        super(StatusCode.USERNAME_ALREADY_EXISTS);
    }
}
