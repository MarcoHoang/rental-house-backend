package com.codegym.dto.response;

import com.codegym.entity.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private RoleName role;
    private String roleName;  // Thêm field mới
    private UserDTO user;
}