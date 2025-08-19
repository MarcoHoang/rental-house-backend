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
    SEND_SUCCESS("09", "success.password.reset.requested"),
    FILE_UPLOAD_SUCCESS("10", "success.file.upload"),
    FILE_DELETE_SUCCESS("11", "success.file.delete"),
    GET_DETAIL_SUCCESS("12", "success.detail.found"),

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
    UNAUTHORIZED("18", "error.auth.unauthorized"),
    ACCOUNT_LOCKED("19", "error.account.locked"),

    // --- Resource Not Found (20-29) ---
    USER_NOT_FOUND("21", "error.user.notFound"), // {0} = userId
    HOST_NOT_FOUND("22", "error.host.notFound"), // {0} = hostId
    HOUSE_NOT_FOUND("23", "error.house.notFound"), // {0} = houseId
    REQUEST_NOT_FOUND("24", "error.request.notFound"), // {0} = requestId
    RENTAL_NOT_FOUND("25", "error.rental.notFound"), // {0} = rentalId
    REVIEW_NOT_FOUND("26", "error.review.notFound"), // {0} = reviewId
    NOTIFICATION_NOT_FOUND("27", "error.notification.notFound"), // {0} = notificationId
    RESOURCE_NOT_FOUND("28", "error.resource.notFound"), // {0} = resourceName

    // --- Host Request (30-39) ---
    USER_ALREADY_HOST("30", "error.request.userAlreadyHost"),
    PENDING_REQUEST_EXISTS("31", "error.request.pendingExists"),
    INVALID_REQUEST_STATUS("32", "error.request.invalidStatus"),

    // --- House Management (40-49) ---
    HOUSE_NOT_AVAILABLE("40", "error.house.notAvailable"),
    INVALID_HOUSE_STATUS("41", "error.house.invalidStatus"), // {0} = status
    GEOCODING_FAILED("42", "error.geogcoding"),

    // --- File Upload (43-49) ---
    FILE_UPLOAD_ERROR("43", "error.file.upload"),
    INVALID_FILE_TYPE("44", "error.file.invalidType"),
    FILE_TOO_LARGE("45", "error.file.tooLarge"),
    FILE_NOT_FOUND("46", "error.file.notFound"),
    FILE_DELETE_ERROR("47", "error.file.delete"),

    // --- Rental Management (50-69) ---
    RENTAL_PERIOD_OVERLAP("50", "error.rental.periodOverlap"),
    CANNOT_UPDATE_ACTIVE_RENTAL("51", "error.rental.cannotUpdateActive"),
    INVALID_CHECKIN_STATUS("52", "error.rental.invalidCheckinStatus"),
    INVALID_CHECKOUT_STATUS("53", "error.rental.invalidCheckoutStatus"),
    CANNOT_RENT_OWN_HOUSE("54", "error.rental.cannotRentOwnHouse"),
    INVALID_START_DATE("55", "error.rental.invalidStartDate"),
    MINIMUM_RENTAL_PERIOD("56", "error.rental.minimumRentalPeriod"),
    PAYMENT_REQUIRED("57", "error.rental.paymentRequired"),
    INSUFFICIENT_BALANCE("58", "error.rental.insufficientBalance"),
    INVALID_CANCEL_STATUS("59", "error.rental.invalidCancelStatus"),
    INVALID_APPROVE_STATUS("60", "error.rental.invalidApproveStatus"),
    INVALID_REJECT_STATUS("61", "error.rental.invalidRejectStatus"),
    CANNOT_CANCEL_WITHIN_24H("62", "error.rental.cannotCancelWithin24h"),

    // --- Review Management (70-79) ---
    CANNOT_REVIEW_NOT_RENTED("70", "error.review.notRented"),
    REVIEW_ALREADY_EXISTS("71", "error.review.alreadyExists"),

    // --- Favorite Management (80-89) ---
    FAVORITE_NOT_FOUND("80", "error.favorite.notFound"),
    FAVORITE_ALREADY_EXISTS("81", "error.favorite.alreadyExists"),
    FAVORITE_NOT_EXISTS("82", "error.favorite.notExists"),
    FAVORITE_TOGGLE_FAILED("83", "error.favorite.toggleFailed"),

    // == SYSTEM & VALIDATION ERRORS (90-99) ==
    DATA_INTEGRITY_VIOLATION("90", "error.data.integrity"),
    ACCESS_DENIED("91", "error.access.denied"),
    FORBIDDEN_ACTION("92", "error.forbidden"),
    PASSWORD_CONFIRMATION_MISMATCH("93", "error.password.mismatch"),

    // Validation & Internal
    PARAM_TYPE_MISMATCH("97", "error.param.typeMismatch"), // {0} = paramName, {1} = expectedType
    VALIDATION_ERROR("98", "error.validation"),
    INTERNAL_ERROR("99", "error.internal"),

    // == FIELD VALIDATION MESSAGES (Dùng cho @Valid) ==
    VALIDATION_USERNAME_NOT_BLANK("V01", "validation.username.notBlank"),
    VALIDATION_USERNAME_SIZE("V02", "validation.username.size"),
    VALIDATION_PHONE_NOT_BLANK("V03", "validation.phone.notBlank"),
    VALIDATION_PHONE_PATTERN("V04", "validation.phone.pattern"),
    VALIDATION_EMAIL_NOT_BLANK("V05", "validation.email.notBlank"),
    VALIDATION_EMAIL_INVALID("V06", "validation.email.invalid"),
    VALIDATION_PASSWORD_NOT_BLANK("V07", "validation.password.notBlank"),
    VALIDATION_PASSWORD_SIZE("V08", "validation.password.size"),
    VALIDATION_REJECT_REASON_NOT_BLANK("V09", "validation.reject.reason.notBlank"),

    // == HOUSE VALIDATION ==
    VALIDATION_HOUSE_TITLE_NOT_BLANK("V10", "validation.house.title.notBlank"),
    VALIDATION_HOUSE_TITLE_SIZE("V11", "validation.house.title.size"),
    VALIDATION_HOUSE_DESCRIPTION_SIZE("V12", "validation.house.description.size"),
    VALIDATION_HOUSE_ADDRESS_NOT_BLANK("V13", "validation.house.address.notBlank"),
    VALIDATION_HOUSE_ADDRESS_SIZE("V14", "validation.house.address.size"),
    VALIDATION_HOUSE_PRICE_POSITIVE("V15", "validation.house.price.positive"),
    VALIDATION_HOUSE_AREA_POSITIVE("V16", "validation.house.area.positive"),
    VALIDATION_HOUSE_TYPE_NOT_NULL("V17", "validation.house.houseType.notNull"),

    // == RENTAL VALIDATION ==
    VALIDATION_RENTAL_START_DATE_NOT_NULL("V20", "validation.rental.startDate.notNull"),
    VALIDATION_RENTAL_END_DATE_NOT_NULL("V21", "validation.rental.endDate.notNull"),
    VALIDATION_RENTAL_GUEST_COUNT_POSITIVE("V22", "validation.rental.guestCount.positive"),
    VALIDATION_RENTAL_MESSAGE_SIZE("V23", "validation.rental.messageToHost.size"),

    // == REVIEW VALIDATION ==
    VALIDATION_REVIEW_RATING_RANGE("V30", "validation.review.rating.range"),
    VALIDATION_REVIEW_COMMENT_SIZE("V31", "validation.review.comment.size"),
    VALIDATION_REVIEW_COMMENT_NOT_BLANK("V32", "validation.review.comment.notBlank");

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
