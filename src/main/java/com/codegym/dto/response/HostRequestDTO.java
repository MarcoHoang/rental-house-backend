package com.codegym.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostRequestDTO {
    private Long id;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    private String userEmail;
    private String username;
    private String fullName;
    private String phone;
    private String status;
    private String reason;
    private LocalDateTime requestDate;
    private LocalDateTime processedDate;
    private String nationalId;
    private String proofOfOwnershipUrl;
}
