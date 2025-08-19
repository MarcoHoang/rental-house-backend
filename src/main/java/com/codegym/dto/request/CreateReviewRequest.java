package com.codegym.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateReviewRequest {
    
    @NotNull(message = "Reviewer ID is required")
    @Min(value = 1, message = "Reviewer ID must be positive")
    private Long reviewerId;
    
    @NotNull(message = "House ID is required")
    @Min(value = 1, message = "House ID must be positive")
    private Long houseId;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
    
    @NotBlank(message = "Comment is required")
    @Size(min = 1, max = 1000, message = "Comment must be between 1 and 1000 characters")
    private String comment;
}
