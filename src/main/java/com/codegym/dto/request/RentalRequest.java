package com.codegym.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalRequest {
    
    @NotNull(message = "ID nhà không được để trống")
    private Long houseId;
    
    @NotNull(message = "Ngày bắt đầu không được để trống")
    @Future(message = "Ngày bắt đầu phải trong tương lai")
    private LocalDateTime startDate;
    
    @NotNull(message = "Ngày kết thúc không được để trống")
    @Future(message = "Ngày kết thúc phải trong tương lai")
    private LocalDateTime endDate;
    
    @Min(value = 1, message = "Số ngày thuê phải ít nhất 1 ngày")
    @Max(value = 365, message = "Số ngày thuê không được vượt quá 365 ngày")
    private Integer numberOfDays;
    
    @DecimalMin(value = "0.0", message = "Số tiền đặt cọc không được âm")
    private Double depositAmount;
    
    @Size(max = 1000, message = "Yêu cầu đặc biệt không được vượt quá 1000 ký tự")
    private String specialRequests;
    
    // Validation method
    public void validateDates() {
        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
            }
            
            if (startDate.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Ngày bắt đầu không được trong quá khứ");
            }
        }
    }
} 