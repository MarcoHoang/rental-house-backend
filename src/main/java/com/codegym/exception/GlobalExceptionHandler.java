package com.codegym.exception;

import com.codegym.dto.ApiResponse;
import com.codegym.utils.MessageUtil;
import com.codegym.utils.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageUtil messageUtil;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        StatusCode statusCode = ex.getStatusCode();
        String message = messageUtil.getMessage(statusCode.getMessageKey(), ex.getArgs());
        return buildErrorResponse(message, statusCode, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleAppException(AppException ex, WebRequest request) {
        StatusCode statusCode = ex.getStatusCode();
        String message = messageUtil.getMessage(statusCode.getMessageKey(), ex.getArgs());
        HttpStatus httpStatus = isConflictError(statusCode) ? HttpStatus.CONFLICT : HttpStatus.BAD_REQUEST;
        return buildErrorResponse(message, statusCode, httpStatus, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<String> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        StatusCode statusCode = StatusCode.VALIDATION_ERROR;
        String mainMessage = messageUtil.getMessage(statusCode.getMessageKey());

        ApiError errorDetails = new ApiError(mainMessage, extractPath(request), validationErrors);
        ApiResponse<ApiError> response = new ApiResponse<>(statusCode.getCode(), mainMessage, errorDetails);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String requiredType = Objects.requireNonNull(ex.getRequiredType()).getSimpleName();
        StatusCode statusCode = StatusCode.VALIDATION_ERROR;
        // Giả sử bạn có key 'error.param.typeMismatch' trong messages.properties
        String message = messageUtil.getMessage("error.param.typeMismatch", ex.getName(), requiredType);
        return buildErrorResponse(message, statusCode, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDatabaseError(DataIntegrityViolationException ex, WebRequest request) {
        StatusCode statusCode = StatusCode.DATA_INTEGRITY_VIOLATION;
        String message = messageUtil.getMessage(statusCode.getMessageKey());
        log.error("Data integrity violation: {}", ex.getMostSpecificCause().getMessage());
        return buildErrorResponse(message, statusCode, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllUncaughtException(Exception ex, WebRequest request) {
        log.error("An unexpected error occurred", ex);
        StatusCode statusCode = StatusCode.INTERNAL_ERROR;
        String message = messageUtil.getMessage(statusCode.getMessageKey());
        return buildErrorResponse(message, statusCode, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ApiResponse<Object>> buildErrorResponse(
            String message, StatusCode statusCode, HttpStatus httpStatus, WebRequest request) {
        ApiError errorDetails = new ApiError(message, extractPath(request));
        ApiResponse<Object> response = new ApiResponse<>(statusCode.getCode(), message, errorDetails);
        return new ResponseEntity<>(response, httpStatus);
    }

    private boolean isConflictError(StatusCode statusCode) {
        return List.of(
                StatusCode.EMAIL_ALREADY_EXISTS,
                StatusCode.PHONE_ALREADY_EXISTS,
                StatusCode.USERNAME_ALREADY_EXISTS,
                StatusCode.DATA_INTEGRITY_VIOLATION,
                StatusCode.USER_ALREADY_HOUSE_RENTER,
                StatusCode.PENDING_REQUEST_EXISTS
        ).contains(statusCode);
    }

    private String extractPath(WebRequest request) {
        try {
            return request.getDescription(false).replace("uri=", "");
        } catch (Exception e) {
            return "N/A";
        }
    }

    @Getter
    private static class ApiError {
        private String message;
        private String path;
        private List<String> details;

        public ApiError(String message, String path) {
            this.message = message;
            this.path = path;
        }

        public ApiError(String message, String path, List<String> details) {
            this.message = message;
            this.path = path;
            this.details = details;
        }
    }
}