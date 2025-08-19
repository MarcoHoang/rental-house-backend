package com.codegym.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleLoginRequest {
    
    @NotBlank(message = "Google ID token is required")
    private String idToken;
    
    private String accessToken;
    
    private String email;
    private String name;
    private String picture;
} 