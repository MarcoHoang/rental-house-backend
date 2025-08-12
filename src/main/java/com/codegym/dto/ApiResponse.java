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

    /**
     * Constructor để tạo một response hoàn chỉnh có dữ liệu.
     * @param code Mã trạng thái (ví dụ: "00", "10")
     * @param message Thông điệp cuối cùng đã được dịch để hiển thị cho người dùng.
     * @param data Dữ liệu trả về (có thể là một đối tượng, danh sách, hoặc null).
     */
    public ApiResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * Constructor để tạo một response không có dữ liệu (ví dụ: xóa thành công).
     * @param code Mã trạng thái (ví dụ: "00")
     * @param message Thông điệp cuối cùng đã được dịch để hiển thị cho người dùng.
     */
    public ApiResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(T data, StatusCode statusCode, MessageSource messageSource, Locale locale) {
        String message = messageSource.getMessage(statusCode.getMessageKey(), null, locale);
        return new ApiResponse<>(statusCode.getCode(), message, data);
    }

    /**
     * Factory method để tạo response thành công KHÔNG CÓ DỮ LIỆU.
     */
    public static <T> ApiResponse<T> success(StatusCode statusCode, MessageSource messageSource, Locale locale) {
        String message = messageSource.getMessage(statusCode.getMessageKey(), null, locale);
        return new ApiResponse<>(statusCode.getCode(), message);
    }
}