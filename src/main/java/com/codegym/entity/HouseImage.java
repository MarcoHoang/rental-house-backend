package com.codegym.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "house_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HouseImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    @Column(nullable = false)
    private String imageUrl;

    private Integer sortOrder;
}
