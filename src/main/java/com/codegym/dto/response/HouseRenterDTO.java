package com.codegym.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HouseRenterDTO {
    private Long id;

    // Thông tin người dùng
    private String fullName;
    private String username;
    private String email;
    private String phone;
    private String avatar;

    // Thông tin người cho thuê nhà
    private String nationalId;
    private String proofOfOwnershipUrl;
    private String address;

    private LocalDateTime approvedDate;

    private boolean approved; // true nếu approvedDate != null
}
