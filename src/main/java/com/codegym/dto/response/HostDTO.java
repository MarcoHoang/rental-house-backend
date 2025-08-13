package com.codegym.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostDTO {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private String nationalId;
    private String proofOfOwnershipUrl;
    private String address;
    private LocalDateTime approvedDate;
    private boolean approved;
    private boolean active;

}

