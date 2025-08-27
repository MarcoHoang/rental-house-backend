package com.codegym.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RejectRentalRequest {
    
    @NotBlank(message = "Reject reason is required")
    @Size(max = 500, message = "Reject reason cannot exceed 500 characters")
    private String reason;
}
