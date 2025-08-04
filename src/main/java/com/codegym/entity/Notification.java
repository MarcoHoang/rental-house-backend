package com.codegym.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Người nhận thông báo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type; // RENTAL_CREATED, RENTAL_CANCELED, REVIEW_RECEIVED, etc.

    @Column(nullable = false)
    private String content;

    private Boolean isRead = false;

    private LocalDateTime createdAt;

    public enum Type {
        RENTAL_CREATED,
        RENTAL_CANCELED,
        REVIEW_RECEIVED,
        LANDLORD_REQUEST_APPROVED,
        LANDLORD_REQUEST_REJECTED,
        GENERAL
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
