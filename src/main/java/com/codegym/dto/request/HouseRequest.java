package com.codegym.dto.request;

import com.codegym.entity.House.HouseType;
import com.codegym.entity.House.Status;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HouseRequest {
    
    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 200, message = "Tiêu đề không được vượt quá 200 ký tự")
    private String title;
    
    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String description;
    
    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 300, message = "Địa chỉ không được vượt quá 300 ký tự")
    private String address;
    
    @NotNull(message = "Giá không được để trống")
    @Positive(message = "Giá phải là số dương")
    @Max(value = 1000000000, message = "Giá không được vượt quá 1 tỷ")
    private Double price;
    
    @NotNull(message = "Diện tích không được để trống")
    @Positive(message = "Diện tích phải là số dương")
    @Max(value = 10000, message = "Diện tích không được vượt quá 10,000 m²")
    private Double area;
    
    @NotNull(message = "Loại nhà không được để trống")
    private HouseType houseType;
    
    @NotNull(message = "Trạng thái không được để trống")
    private Status status;
    
    private Double latitude;
    private Double longitude;
    
    private List<String> imageUrls;
}
