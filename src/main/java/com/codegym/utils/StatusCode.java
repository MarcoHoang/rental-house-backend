package com.codegym.utils;

import lombok.Getter;

@Getter
public enum StatusCode {

    // ✅ Success
    SUCCESS("00", "Thành công"),

    // ✅ Business logic errors
    EMAIL_ALREADY_EXISTS("01", "Email đã tồn tại"),
    PHONE_ALREADY_EXISTS("02", "Số điện thoại đã tồn tại"),
    INVALID_PASSWORD("03", "Mật khẩu không hợp lệ"),
    DUPLICATE_OLD_PASSWORD("04", "Mật khẩu mới không được trùng với mật khẩu cũ"),
    USER_NOT_FOUND("05", "Không tìm thấy người dùng"),
    TOKEN_INVALID("06", "Token không hợp lệ"),
    ROLE_NOT_FOUND("07", "Không tìm thấy vai trò"),
    EMAIL_EMPTY("10", "Email không được để trống"),
    EMAIL_NOT_REGISTERED("11", "Email chưa được đăng ký"),

    // ✅ System & validation
    VALIDATION_ERROR("98", "Lỗi xác thực"),
    INTERNAL_ERROR("99", "Lỗi hệ thống");

    private final String code;
    private final String message;

    StatusCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    // ✅ Optional: Tìm theo mã
    public static StatusCode fromCode(String code) {
        for (StatusCode sc : values()) {
            if (sc.code.equals(code)) {
                return sc;
            }
        }
        throw new IllegalArgumentException("Mã lỗi không hợp lệ: " + code);
    }
}
