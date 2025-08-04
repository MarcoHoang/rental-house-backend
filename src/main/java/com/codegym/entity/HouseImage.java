package com.codegym.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "house_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HouseImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mỗi ảnh thuộc về một căn nhà
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    @Column(nullable = false)
    private String imageUrl; // Đường dẫn ảnh

    private Integer sortOrder; // Để sắp xếp thứ tự hiển thị ảnh (nếu cần)
}
