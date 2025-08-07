package com.codegym.utils;

import lombok.Getter;

@Getter
public enum StatusCode {

    // == SUCCESS CODES (00-09) ==
    SUCCESS("00", "success.general"),
    CREATED_SUCCESS("01", "success.created"),
    UPDATED_SUCCESS("02", "success.updated"),
    DELETED_SUCCESS("03", "success.deleted"),
    GET_LIST_SUCCESS("04", "success.list.found"),
    PASSWORD_CHANGED("05", "success.password.changed"),
    PROFILE_UPDATED("06", "success.profile.updated"),
    LOGIN_SUCCESS("07", "success.auth.login"),
    REGISTER_SUCCESS("08", "success.auth.register"),

    // == BUSINESS LOGIC ERRORS (10-89) ==

    // --- Authentication & User Management (10-19) ---
    EMAIL_ALREADY_EXISTS("10", "error.email.exists"),
    PHONE_ALREADY_EXISTS("11", "error.phone.exists"),
    USERNAME_ALREADY_EXISTS("12", "error.username.exists"),
    INVALID_PASSWORD("13", "error.password.invalid"),
    DUPLICATE_OLD_PASSWORD("14", "error.password.duplicateOld"),
    TOKEN_INVALID("15", "error.token.invalid"),
    ROLE_NOT_FOUND("16", "error.role.notFound"),
    INVALID_CREDENTIALS("17", "error.auth.invalidCredentials"),

    // --- Resource Not Found (20-29) ---
    USER_NOT_FOUND("21", "error.user.notFound"),
    HOUSE_RENTER_NOT_FOUND("22", "error.houserenter.notFound"), // Sửa ở đây
    HOUSE_NOT_FOUND("23", "error.house.notFound"),
    REQUEST_NOT_FOUND("24", "error.request.notFound"),
    RENTAL_NOT_FOUND("25", "error.rental.notFound"),
    REVIEW_NOT_FOUND("26", "error.review.notFound"),
    NOTIFICATION_NOT_FOUND("27", "error.notification.notFound"),

    // --- House Renter Request (30-39) ---
    USER_ALREADY_HOUSE_RENTER("30", "error.request.userAlreadyHouseRenter"), // Sửa ở đây
    PENDING_REQUEST_EXISTS("31", "error.request.pendingExists"),
    INVALID_REQUEST_STATUS("32", "error.request.invalidStatus"),

    // --- House Management (40-49) ---
    HOUSE_NOT_AVAILABLE("40", "error.house.notAvailable"),
    INVALID_HOUSE_STATUS("41", "error.house.invalidStatus"),

    // --- Rental Management (50-59) ---
    RENTAL_PERIOD_OVERLAP("50", "error.rental.periodOverlap"),
    CANNOT_UPDATE_ACTIVE_RENTAL("51", "error.rental.cannotUpdateActive"),
    INVALID_CHECKIN_STATUS("52", "error.rental.invalidCheckinStatus"),
    INVALID_CHECKOUT_STATUS("53", "error.rental.invalidCheckoutStatus"),

    // --- Review Management (60-69) ---
    CANNOT_REVIEW_NOT_RENTED("60", "error.review.notRented"),
    REVIEW_ALREADY_EXISTS("61", "error.review.alreadyExists"),


    // == SYSTEM & VALIDATION ERRORS (90-99) ==
    DATA_INTEGRITY_VIOLATION("90", "error.data.integrity"),
    ACCESS_DENIED("91", "error.access.denied"),

    VALIDATION_ERROR("98", "error.validation"),
    INTERNAL_ERROR("99", "error.internal");





    private final String code;
    private final String messageKey;

    StatusCode(String code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    public static StatusCode fromCode(String code) {
        for (StatusCode sc : values()) {
            if (sc.code.equals(code)) {
                return sc;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }
}