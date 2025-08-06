package com.codegym.exception;

import com.codegym.dto.ApiResponse;
import com.codegym.utils.MessageUtil;
import com.codegym.utils.StatusCode;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageUtil messageUtil;

    // Xử lý các lỗi do client cung cấp dữ liệu không tìm thấy trong DB.
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<ApiError>> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        // Ghi chú: Bạn có thể muốn tạo một StatusCode.ENTITY_NOT_FOUND ("05", "Không tìm thấy tài nguyên")
        // để dùng chung, thay vì USER_NOT_FOUND quá cụ thể.
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), extractPath(request));
        return new ResponseEntity<>(new ApiResponse<>(StatusCode.USER_NOT_FOUND, error), HttpStatus.NOT_FOUND);
    }

    // Xử lý các lỗi nghiệp vụ được định nghĩa sẵn
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiResponse<ApiError>> handleDuplicateEmail(DuplicateEmailException ex, WebRequest request) {
        ApiError error = new ApiError(HttpStatus.CONFLICT, ex.getMessage(), extractPath(request));
        return new ResponseEntity<>(new ApiResponse<>(StatusCode.EMAIL_ALREADY_EXISTS, error), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicatePhoneException.class)
    public ResponseEntity<ApiResponse<ApiError>> handleDuplicatePhone(DuplicatePhoneException ex, WebRequest request) {
        ApiError error = new ApiError(HttpStatus.CONFLICT, ex.getMessage(), extractPath(request));
        return new ResponseEntity<>(new ApiResponse<>(StatusCode.PHONE_ALREADY_EXISTS, error), HttpStatus.CONFLICT);
    }

    // Xử lý các lỗi nghiệp vụ chung, sử dụng message từ properties file
    @ExceptionHandler(BaseAppException.class)
    public ResponseEntity<ApiResponse<ApiError>> handleBaseAppException(BaseAppException ex, WebRequest request) {
        String message = messageUtil.getMessage(ex.getMessage());
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, message, extractPath(request));

        // Dựa vào message key để xác định StatusCode phù hợp
        StatusCode statusCode = switch (ex.getMessage()) {
            case "error.invalidPassword" -> StatusCode.INVALID_PASSWORD;
            case "error.duplicateOldPassword" -> StatusCode.DUPLICATE_OLD_PASSWORD;
            // Thêm các case khác tại đây
            default -> StatusCode.VALIDATION_ERROR;
        };

        return new ResponseEntity<>(new ApiResponse<>(statusCode, error), HttpStatus.BAD_REQUEST);
    }

    // Xử lý khi tham số đường dẫn (path variable) có kiểu dữ liệu không đúng
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<ApiError>> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String requiredType = Objects.requireNonNull(ex.getRequiredType()).getSimpleName();
        String message = String.format("Tham số '%s' phải là kiểu '%s'.", ex.getName(), requiredType);
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, message, extractPath(request));
        return new ResponseEntity<>(new ApiResponse<>(StatusCode.VALIDATION_ERROR, error), HttpStatus.BAD_REQUEST);
    }

    // Xử lý các lỗi ràng buộc dữ liệu từ DB (VD: unique constraint)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<ApiError>> handleDatabaseError(DataIntegrityViolationException ex, WebRequest request) {
        ApiError error = new ApiError(HttpStatus.CONFLICT, "Dữ liệu vi phạm ràng buộc. Có thể do trùng lặp thông tin.", extractPath(request));
        // Bạn có thể phân tích sâu hơn `ex.getMostSpecificCause().getMessage()` để tìm ra StatusCode chính xác hơn
        return new ResponseEntity<>(new ApiResponse<>(StatusCode.VALIDATION_ERROR, error), HttpStatus.CONFLICT);
    }

    // Xử lý các lỗi chung không lường trước được
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ApiError>> handleAllUncaughtException(Exception ex, WebRequest request) {
        // Luôn ghi log cho các lỗi không mong muốn để debug
        logger.error("An unexpected error occurred", ex);
        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Có lỗi xảy ra trong hệ thống, vui lòng thử lại sau.", extractPath(request));
        return new ResponseEntity<>(new ApiResponse<>(StatusCode.INTERNAL_ERROR, error), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Ghi đè phương thức xử lý lỗi @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Dữ liệu không hợp lệ.", extractPath(request), validationErrors);
        ApiResponse<ApiError> response = new ApiResponse<>(StatusCode.VALIDATION_ERROR, error);

        return new ResponseEntity<>(response, headers, status);
    }

    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}