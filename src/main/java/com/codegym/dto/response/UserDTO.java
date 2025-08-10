package com.codegym.dto.response;

import com.codegym.entity.RoleName;
import lombok.*;

import java.time.LocalDate;

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
    private String email;
    private LocalDate birthDate;
    private boolean active;
    private RoleName roleName;
}
