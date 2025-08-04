package com.codegym.dto.response;

import com.codegym.entity.Rental.Status;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalDTO {
    private Long id;
    private Long houseId;
    private Long renterId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
