package com.codegym.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "{validation.password.notblank}")
    private String oldPassword;

    @NotBlank(message = "{validation.password.notblank}")
    @Size(min = 6, max = 20, message = "{validation.password.size}")
    private String newPassword;

    @NotBlank(message = "{validation.password.confirm.notblank}")
    private String confirmPassword;
}