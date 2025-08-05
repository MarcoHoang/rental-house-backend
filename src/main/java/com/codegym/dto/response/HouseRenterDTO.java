package com.codegym.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseRenterDTO {
    private Long id;
    private String fullName;
    private String address;
    private String phone;
    private String avatar;
    private boolean approved;

    private String username;
    private String email;
}
