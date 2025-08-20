package com.codegym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostDetailDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String avatarUrl;
    private boolean active;
    private LocalDate birthDate;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private Long totalHouses;
    private Long totalRentals;
    private Double totalEarnings;
    private List<HouseSummaryDTO> houses;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HouseSummaryDTO {
        private Long id;
        private String title;
        private String address;
        private String status;
        private Double price;
        private Long totalRentals;
    }
}
