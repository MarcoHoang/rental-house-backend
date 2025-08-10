package com.codegym.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "houses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class House extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_renter_id", nullable = false)
    private User houseRenter;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private String address;

    private Double price;

    private double area;

    private Double latitude;
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "house_type", nullable = false)
    private HouseType houseType;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HouseImage> images;

    public enum Status {
        AVAILABLE,
        RENTED,
        INACTIVE
    }

    public enum HouseType {
        APARTMENT,
        VILLA,
        TOWNHOUSE,
        BOARDING_HOUSE,
        WHOLE_HOUSE
    }
}
