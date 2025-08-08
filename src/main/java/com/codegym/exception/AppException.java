package com.codegym.exception;

import com.codegym.utils.StatusCode;
import lombok.Getter;

@Getter
// Bỏ "abstract" để có thể tạo đối tượng trực tiếp từ class này
public class AppException extends RuntimeException {

    private final StatusCode statusCode;
    private final Object[] args;

    // Constructor chính
    public AppException(StatusCode statusCode, Object... args) {
        super(statusCode.getMessageKey());
        this.statusCode = statusCode;
        this.args = args;
    }
}