package com.codegym.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class UserDetailDTO {
    private Long id;
    private String fullName;
    private String address;
    private String phone;
    private String avatarUrl;
    private String email;
    private LocalDate birthDate;
    private boolean active;

    private BigDecimal totalSpent;
    private List<RentalHistoryDTO> rentalHistory;
}