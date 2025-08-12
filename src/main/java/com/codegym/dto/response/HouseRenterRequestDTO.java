package com.codegym.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HouseRenterRequestDTO {
    private Long id;
    private Long userId;
    private String userEmail;
    private String username;
    private String status;
    private String reason;
    private LocalDateTime requestDate;
    private LocalDateTime processedDate;
}
