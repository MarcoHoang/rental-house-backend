package com.codegym.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long id;
    private Long reviewerId;
    private String reviewerName;
    private Long houseId;
    private Integer rating;
    private String comment;
    private Boolean isVisible;
    private LocalDateTime createdAt;
}
