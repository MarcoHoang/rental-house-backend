package com.codegym.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id", nullable = false)
    private User renter;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // PENDING, APPROVED, CHECKED_IN, CHECKED_OUT, CANCELED

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Double totalPrice;

    public enum Status {
        SCHEDULED,
        PENDING,      // Người dùng vừa đặt
        APPROVED,     // Chủ nhà xác nhận
        CHECKED_IN,   // Khách đã nhận phòng
        CHECKED_OUT,  // Khách đã trả phòng
        CANCELED      // Hủy đặt phòng
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
