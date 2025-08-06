package com.codegym.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // Chỉ serialize các trường không null
public class ApiError {

    private String timestamp;
    private String path;
    private String message;
    private List<String> subErrors; // Dành cho các lỗi validation chi tiết

    public ApiError(String message, String path) {
        this.timestamp = LocalDateTime.now().toString();
        this.message = message;
        this.path = path;
    }

    public ApiError(String message, String path, List<String> subErrors) {
        this(message, path);
        this.subErrors = subErrors;
    }
}