package com.codegym.exception;

import com.codegym.utils.StatusCode;


/**
 * Exception chuyên dùng cho các trường hợp không tìm thấy tài nguyên.
 * Kế thừa trực tiếp từ AppException và được xử lý riêng trong GlobalExceptionHandler
 * để trả về HTTP Status 404 NOT FOUND.
 */
public class ResourceNotFoundException extends AppException {

    /**
     * Constructor chính và duy nhất.
     * @param statusCode Mã trạng thái cụ thể (ví dụ: USER_NOT_FOUND, HOUSE_NOT_FOUND).
     * @param args Các tham số cho message (ví dụ: ID của tài nguyên không tìm thấy).
     */
    public ResourceNotFoundException(StatusCode statusCode, Object... args) {
        super(statusCode, args);
    }
}