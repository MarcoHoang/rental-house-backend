package com.codegym.exception;

import com.codegym.utils.StatusCode;

public class DuplicatePhoneException extends AppException {
    public DuplicatePhoneException() {
        super(StatusCode.PHONE_ALREADY_EXISTS);
    }
}
