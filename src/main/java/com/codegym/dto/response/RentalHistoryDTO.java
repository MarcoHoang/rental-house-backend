package com.codegym.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class RentalHistoryDTO {
    private Long houseId;
    private String houseTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalPrice;
}
