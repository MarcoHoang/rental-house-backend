package com.codegym.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LandlordRequestDTO {
    private Long id;
    private Long userId;
    private String userEmail;
    private String userName;
    private String status;
    private String reason;
    private LocalDateTime requestDate;
    private LocalDateTime processedDate;
}
