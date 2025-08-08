package com.codegym.exception;

import com.codegym.utils.StatusCode;

public class DuplicateEmailException extends AppException {
    public DuplicateEmailException() {
        super(StatusCode.EMAIL_ALREADY_EXISTS);
    }
}
