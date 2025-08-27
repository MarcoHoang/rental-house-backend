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
    
    // Thông tin nhà
    private Long houseId;
    private String houseTitle;
    private String houseAddress;
    private String houseType;
    private String houseDescription;
    private Double housePrice;
    private Double houseArea;
    private String houseStatus;
    
    // Thông tin người thuê
    private Long renterId;
    private String renterName;
    private String renterEmail;
    private String renterPhone;
    private String renterAddress;
    private String renterAvatar;
    
    // Thông tin chủ nhà
    private Long hostId;
    private String hostName;
    private String hostEmail;
    private String hostPhone;
    private String hostAddress;
    private String hostAvatar;
    
    // Thông tin hợp đồng
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Status status;
    private Double totalPrice;
    private Double monthlyRent;
    private Integer duration;
    private Integer guestCount;
    private String messageToHost;
    private String rejectReason;
    private String cancelReason;
    
    // Thông tin xử lý
    private LocalDateTime approvedAt;
    private String approvedByName;
    private LocalDateTime rejectedAt;
    private String rejectedByName;
    private LocalDateTime canceledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
