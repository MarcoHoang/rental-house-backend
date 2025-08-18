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
    private String houseTitle;
    private String houseAddress;
    private Long renterId;
    private String renterName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Status status;
    private Double totalPrice;
    private Integer guestCount;
    private String messageToHost;
    private String rejectReason;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
