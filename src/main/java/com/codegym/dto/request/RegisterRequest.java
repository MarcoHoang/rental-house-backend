package com.codegym.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "{validation.username.notBlank}")
    @Size(max = 100, message = "{validation.username.size}")
    private String username;

    @NotBlank(message = "{validation.phone.notBlank}")
    @Pattern(regexp = "^0\\d{9}$", message = "{validation.phone.pattern}")
    private String phone;

    @NotBlank(message = "{validation.email.notBlank}")
    @Email(message = "{validation.email.invalid}")
    private String email;

    @NotBlank(message = "{validation.password.notBlank}")
    @Size(min = 6, message = "{validation.password.size}")
    private String password;

    private String address;
}