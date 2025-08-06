package com.codegym.dto;

import com.codegym.utils.StatusCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private String code;
    private String message;
    private T data;

    // Constructor chính để nhận StatusCode và dữ liệu
    public ApiResponse(StatusCode statusCode, T data) {
        this.code = statusCode.getCode();
        this.message = statusCode.getMessage();
        this.data = data;
    }

    // Constructor tiện ích cho các trường hợp không có dữ liệu trả về (VD: delete, change password)
    public ApiResponse(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.message = statusCode.getMessage();
    }
}