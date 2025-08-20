package com.codegym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostStatisticsDTO {
    
    private Long hostId;
    private String period;
    
    // Thống kê tổng quan
    private Integer totalRentals;
    private BigDecimal totalRevenue;
    private BigDecimal netRevenue;
    private Double occupancyRate;
    
    // Thống kê so sánh với kỳ trước
    private BigDecimal revenueChange;
    private Double revenueChangePercentage;
    private Integer rentalChange;
    private Double rentalChangePercentage;
    
    // Top nhà được thuê nhiều nhất
    private List<HouseRentalStats> topHouses;
    
    // Nhà được thuê ít nhất
    private List<HouseRentalStats> leastRentedHouses;
    
    // Xu hướng doanh thu theo tháng
    private List<MonthlyRevenue> monthlyTrend;
    
    // Thông tin thuế và phí
    private BigDecimal taxAmount;
    private BigDecimal platformFee;
    private BigDecimal totalDeductions;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HouseRentalStats {
        private Long houseId;
        private String houseTitle;
        private String address;
        private Integer rentalCount;
        private BigDecimal totalRevenue;
        private BigDecimal averageRevenue;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyRevenue {
        private String month;
        private String year;
        private BigDecimal revenue;
        private Integer rentalCount;
    }
}
