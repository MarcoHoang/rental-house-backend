package com.codegym.utils;

import lombok.Getter;

@Getter
public enum StatusCode {

    // ====================================================================
    // == SUCCESS CODES (00-09) ==
    // ====================================================================
    SUCCESS("00", "success.general"),
    CREATED_SUCCESS("00", "success.created"),
    UPDATED_SUCCESS("00", "success.updated"),
    DELETED_SUCCESS("00", "success.deleted"),
    GET_LIST_SUCCESS("00", "success.list.found"),
    PASSWORD_CHANGED("00", "success.password.changed"),
    PROFILE_UPDATED("00", "success.profile.updated"),


    // ====================================================================
    // == BUSINESS LOGIC ERRORS (10-89) ==
    // ====================================================================
    EMAIL_ALREADY_EXISTS("10", "error.email.exists"),
    PHONE_ALREADY_EXISTS("11", "error.phone.exists"),
    INVALID_PASSWORD("12", "error.password.invalid"),
    DUPLICATE_OLD_PASSWORD("13", "error.password.duplicateOld"),
    USER_NOT_FOUND("14", "error.user.notFound"),
    TOKEN_INVALID("15", "error.token.invalid"),
    ROLE_NOT_FOUND("16", "error.role.notFound"),
    EMAIL_NOT_REGISTERED("17", "error.email.notRegistered"),

    // -- Data & Resource Errors --
    RESOURCE_NOT_FOUND("20", "error.resource.notFound"),
    DATA_INTEGRITY_VIOLATION("21", "error.data.integrity"),


    // ====================================================================
    // == SYSTEM & VALIDATION ERRORS (90-99) ==
    // ====================================================================
    VALIDATION_ERROR("98", "error.validation"),
    INTERNAL_ERROR("99", "error.internal");


    private final String code;
    private final String messageKey; // Đổi tên từ 'message' thành 'messageKey' cho rõ ràng

    StatusCode(String code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    /**
     * Optional: Tìm StatusCode dựa trên mã code.
     * Hữu ích trong một số trường hợp cần chuyển đổi ngược.
     * @param code Mã lỗi (ví dụ: "10")
     * @return StatusCode tương ứng
     */
    public static StatusCode fromCode(String code) {
        for (StatusCode sc : values()) {
            if (sc.code.equals(code)) {
                return sc;
            }
        }
        throw new IllegalArgumentException("Mã lỗi không hợp lệ: " + code);
    }
}