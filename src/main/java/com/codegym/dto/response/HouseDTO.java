package com.codegym.dto.response;

import com.codegym.entity.House.HouseType;
import com.codegym.entity.House.Status;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HouseDTO {
    private Long id;
    private Long houseRenterId;
    private String houseRenterName;

    private String title;
    private String description;
    private String address;
    private Double price;
    private double area;

    private Double latitude;
    private Double longitude;

    private Status status;
    private HouseType houseType;

    private List<String> imageUrls;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
