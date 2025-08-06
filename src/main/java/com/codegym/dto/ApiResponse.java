package com.codegym.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Chỉ serialize các trường có giá trị, bỏ qua các trường null
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
}