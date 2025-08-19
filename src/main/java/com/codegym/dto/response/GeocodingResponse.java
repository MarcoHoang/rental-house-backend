package com.codegym.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeocodingResponse {
    private boolean isValid;
    private Double latitude;
    private Double longitude;
    private String formattedAddress;
    private String originalAddress;
    private String message;
}
