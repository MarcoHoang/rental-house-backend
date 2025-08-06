package com.codegym.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
// Chỉ bao gồm các trường không null trong JSON output
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private final HttpStatus status;
    private final String message;
    private final String path;
    private final LocalDateTime timestamp;
    private List<String> details; // Dùng cho lỗi validation

    // Constructor cho lỗi chung
    public ApiError(HttpStatus status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor cho lỗi validation
    public ApiError(HttpStatus status, String message, String path, List<String> details) {
        this(status, message, path);
        this.details = details;
    }
}