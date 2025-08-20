package com.codegym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostStatsDTO {
    private Long totalHosts;
    private Long activeHosts;
    private Long totalHouses;
    private Long activeHouses;
    private Long totalRentals;
    private Double totalEarnings;
}
