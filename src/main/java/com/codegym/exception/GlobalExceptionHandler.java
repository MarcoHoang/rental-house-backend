package com.codegym.exception;

import com.codegym.dto.ApiResponse;
import com.codegym.utils.MessageUtil;
import com.codegym.utils.StatusCode;
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

        // Đã sửa: Sử dụng constructor (String code, String message, T data)
        ApiResponse<ApiError> response = new ApiResponse<>(statusCode.getCode(), mainMessage, errorDetails);

        return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<ApiError>> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        StatusCode statusCode = ex.getStatusCode();
        String message = messageUtil.getMessage(statusCode.getMessageKey(), ex.getArgs());
        return buildErrorResponse(message, statusCode, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({DuplicateEmailException.class, DuplicatePhoneException.class})
    public ResponseEntity<ApiResponse<ApiError>> handleDuplicateInfo(RuntimeException ex, WebRequest request) {
        StatusCode statusCode;
        if (ex instanceof DuplicateEmailException) {
            statusCode = StatusCode.EMAIL_ALREADY_EXISTS;
        } else { // DuplicatePhoneException
            statusCode = StatusCode.PHONE_ALREADY_EXISTS;
        }
        String message = messageUtil.getMessage(statusCode.getMessageKey());
        return buildErrorResponse(message, statusCode, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<ApiError>> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String requiredType = Objects.requireNonNull(ex.getRequiredType()).getSimpleName();
        StatusCode statusCode = StatusCode.VALIDATION_ERROR;
        String message = messageUtil.getMessage("error.param.typeMismatch", ex.getName(), requiredType);
        return buildErrorResponse(message, statusCode, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<ApiError>> handleDatabaseError(DataIntegrityViolationException ex, WebRequest request) {
        StatusCode statusCode = StatusCode.DATA_INTEGRITY_VIOLATION;
        String message = messageUtil.getMessage(statusCode.getMessageKey());
        log.error("Data integrity violation: {}", ex.getMostSpecificCause().getMessage());
        return buildErrorResponse(message, statusCode, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ApiError>> handleAllUncaughtException(Exception ex, WebRequest request) {
        log.error("An unexpected error occurred", ex);
        StatusCode statusCode = StatusCode.INTERNAL_ERROR;
        String message = messageUtil.getMessage(statusCode.getMessageKey());
        return buildErrorResponse(message, statusCode, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Phương thức trợ giúp đã được cập nhật để sử dụng constructor đúng của ApiResponse.
     */
    private ResponseEntity<ApiResponse<ApiError>> buildErrorResponse(
            String message, StatusCode statusCode, HttpStatus httpStatus, WebRequest request) {
        ApiError errorDetails = new ApiError(message, extractPath(request));

        // Đã sửa: Sử dụng constructor (String code, String message, T data)
        ApiResponse<ApiError> response = new ApiResponse<>(statusCode.getCode(), message, errorDetails);

        return new ResponseEntity<>(response, httpStatus);
    }

    private String extractPath(WebRequest request) {
        try {
            return request.getDescription(false).replace("uri=", "");
        } catch (Exception e) {
            return "N/A";
        }
    }
}