package com.codegym.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "houses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Một chủ nhà có thể có nhiều căn
    @JoinColumn(name = "houserenter_id", nullable = false)
    private User houseRenter;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private String address;

    private Double price;

    private double area;

    private Double latitude;   // để hiển thị trên bản đồ
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status; // AVAILABLE, RENTED, INACTIVE

    @Enumerated(EnumType.STRING)
    @Column(name = "house_type", nullable = false)
    private HouseType houseType;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HouseImage> images;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
