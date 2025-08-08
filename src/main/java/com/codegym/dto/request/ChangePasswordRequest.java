package com.codegym.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "{validation.password.notBlank}")
    private String oldPassword;

    @NotBlank(message = "{validation.password.notBlank}")
    @Size(min = 6, max = 20, message = "{validation.password.size}")
    private String newPassword;

    @NotBlank(message = "{validation.password.confirm.notBlank}")
    private String confirmPassword;
}