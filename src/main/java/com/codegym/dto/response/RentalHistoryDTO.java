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
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private Double price;
}