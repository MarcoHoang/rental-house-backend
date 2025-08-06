package com.codegym.exception;

import com.codegym.utils.StatusCode;
import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final StatusCode statusCode = StatusCode.RESOURCE_NOT_FOUND;
    private final Object[] args;

    public ResourceNotFoundException(String resourceName, Object... args) {
        // message ở đây chỉ dùng để ghi log, không hiển thị cho user
        super(String.format("%s not found with params: %s", resourceName, java.util.Arrays.toString(args)));
        this.args = new Object[]{resourceName, java.util.Arrays.toString(args)};
    }
}