package com.codegym.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String address;
    private String phone;
    private boolean active;
    private long role;
}

