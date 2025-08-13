package com.codegym.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class HostDetailAdminDTO {
    private Long id;
    private String avatarUrl;
    private String username;
    private String fullName;
    private String phone;
    private String email;
    private boolean active;
    private String address;
    private BigDecimal totalRevenue;
    private List<HouseDTO> houses;
}