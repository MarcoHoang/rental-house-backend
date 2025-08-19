package com.codegym.dto;

import com.codegym.utils.StatusCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.MessageSource;

import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private String code;
    private String message;
    private T data;

    public ApiResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(T data, StatusCode statusCode, MessageSource messageSource, Locale locale) {
        String message = messageSource.getMessage(statusCode.getMessageKey(), null, locale);
        return new ApiResponse<>(statusCode.getCode(), message, data);
    }

    public static <T> ApiResponse<T> success(StatusCode statusCode, MessageSource messageSource, Locale locale) {
        String message = messageSource.getMessage(statusCode.getMessageKey(), null, locale);
        return new ApiResponse<>(statusCode.getCode(), message);
    }

    public static <T> ApiResponse<T> error(String code, String message, MessageSource messageSource, Locale locale) {
        return new ApiResponse<>(code, message);
    }

    public static <T> ApiResponse<T> error(StatusCode statusCode, MessageSource messageSource, Locale locale) {
        String message = messageSource.getMessage(statusCode.getMessageKey(), null, locale);
        return new ApiResponse<>(statusCode.getCode(), message);
    }
}