package com.codegym.dto.response;

import com.codegym.entity.RoleName;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String fullName;
    private String address;
    private String phone;
    private String avatarUrl;
    private String username;
    private String email;
    private boolean active;
    private RoleName roleName;
}
