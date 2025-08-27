// Trong package: com.codegym.dto.response
package com.codegym.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class UserDetailAdminDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String avatarUrl;
    private boolean active;
    private LocalDate birthDate;
    private String address;

    private Double totalSpent;
    private List<RentalHistoryDTO> rentalHistory;
}