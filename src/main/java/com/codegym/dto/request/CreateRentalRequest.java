package com.codegym.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRentalRequest {
    
    @NotNull(message = "House ID is required")
    private Long houseId;
    
    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;
    
    @NotNull(message = "End date is required")
    private LocalDateTime endDate;
    
    @Min(value = 1, message = "Guest count must be at least 1")
    @Max(value = 20, message = "Guest count cannot exceed 20")
    private Integer guestCount;
    
    @Size(max = 1000, message = "Message to host cannot exceed 1000 characters")
    private String messageToHost;
}
