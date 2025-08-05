package com.codegym.exception;

import com.codegym.dto.ApiResponse;
import com.codegym.utils.MessageUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageUtil messageUtil;

    /**
     * Xử lý lỗi không tìm thấy Entity (ví dụ: khi gọi findById nhưng không tồn tại).
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<ApiError>> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        String path = extractPath(request);
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage(), path);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(404, messageUtil.getMessage("Không tìm thấy dữ liệu"), error));
    }

    /**
     * Xử lý lỗi chung không xác định.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ApiError>> handleAllExceptions(Exception ex, WebRequest request) {
        ex.printStackTrace(); // log lỗi
        String path = extractPath(request);
        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), path);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(500, messageUtil.getMessage("Lỗi hệ thống. Vui lòng thử lại sau."), error));
    }

    /**
     * Xử lý IllegalArgumentException (ví dụ: check logic trong code).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<ApiError>> handleBadRequest(IllegalArgumentException ex, WebRequest request) {
        String path = extractPath(request);
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), path);
        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(400, messageUtil.getMessage("Yêu cầu không hợp lệ"), error));
    }

    /**
     * Xử lý lỗi database (DataAccessException: lỗi SQL, truy vấn sai, constraint DB...).
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<ApiError>> handleDatabaseError(DataAccessException ex, WebRequest request) {
        String path = extractPath(request);
        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Database error: " + ex.getMessage(), path);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(500, messageUtil.getMessage("Lỗi truy vấn cơ sở dữ liệu"), error));
    }

    /**
     * Xử lý lỗi truyền sai kiểu tham số (ví dụ: @PathVariable không phải int).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<ApiError>> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String path = extractPath(request);
        String errorMsg = "Invalid parameter: " + ex.getName() + " must be of type " +
                Objects.requireNonNull(ex.getRequiredType()).getSimpleName();
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), errorMsg, path);
        return ResponseEntity.badRequest().body(new ApiResponse<>(400, messageUtil.getMessage("Yêu cầu không hợp lệ"), error));
    }

    /**
     * Xử lý lỗi email đã tồn tại (ví dụ: đăng ký trùng email).
     */
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiResponse<ApiError>> handleDuplicateEmail(DuplicateEmailException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        ApiError error = new ApiError(409, ex.getMessage(), path);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(409, ex.getMessage(), error));
    }


    /**
     * Xử lý lỗi phone đã tồn tại (ví dụ: đăng ký trùng email).
     */
    @ExceptionHandler(DuplicatePhoneException.class)
    public ResponseEntity<ApiResponse<ApiError>> handleDuplicateEmail(DuplicatePhoneException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        ApiError error = new ApiError(409, ex.getMessage(), path);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(409, ex.getMessage(), error));
    }


    /**
     * Xử lý lỗi nghiệp vụ tùy chỉnh (do lập trình viên ném ra).
     */
    @ExceptionHandler(BaseAppException.class)
    public ResponseEntity<ApiResponse<Object>> handleBaseAppException(BaseAppException ex) {
        String msg = messageUtil.getMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(new ApiResponse<>(400, msg, null));
    }

    /**
     * Xử lý lỗi @Valid với @RequestBody.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            org.springframework.http.HttpHeaders headers,
            org.springframework.http.HttpStatusCode status,
            WebRequest request) {
        String path = extractPath(request);
        StringBuilder sb = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            sb.append(error.getDefaultMessage()).append("; ");
        }
        ApiError apiError = new ApiError(400, sb.toString(), path);
        return ResponseEntity.badRequest().body(new ApiResponse<>(400, sb.toString(), null));
    }

    /**
     * Xử lý lỗi validate @RequestParam, @PathVariable với @Validated.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(violation -> messageUtil.getMessage(violation.getMessage()))
                .findFirst()
                .orElse(messageUtil.getMessage("Dữ liệu không hợp lệ"));
        return ResponseEntity.badRequest().body(new ApiResponse<>(400, message, null));
    }

    /**
     * Trích xuất path từ WebRequest (uri=/api/...).
     */
    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}


