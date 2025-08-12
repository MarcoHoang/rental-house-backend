// Trong package: com.codegym.dto.response
package com.codegym.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class RentalHistoryDTO {
    private Long houseId;
    private String houseName;
    private LocalDate checkinDate;  // Vẫn dùng LocalDate để hiển thị gọn gàng
    private LocalDate checkoutDate; // Vẫn dùng LocalDate
    private Double price;           // <-- ĐÃ SỬA: từ BigDecimal sang Double
}