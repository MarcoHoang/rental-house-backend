package com.codegym.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Login request")
public class LoginRequest {

    @NotBlank(message = "{validation.email.notBlank}")
    @Email(message = "{validation.email.invalid}")
    @Schema(description = "Email address", example = "user@example.com")
    private String email;

    @NotBlank(message = "{validation.password.notBlank}")
    @Size(min = 6, message = "{validation.password.size}")
    @Schema(description = "Password", example = "123456@Aa")
    private String password;
}

