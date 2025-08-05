package com.codegym.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Mật khẩu cũ không được để trống.")
    private String oldPassword;

    @NotBlank(message = "Mật khẩu mới không được để trống.")
    @Size(min = 6, max = 20, message = "Mật khẩu phải có từ 6 đến 20 ký tự.")
    private String newPassword;

    @NotBlank(message = "Vui lòng xác nhận mật khẩu mới.")
    private String confirmPassword;
}