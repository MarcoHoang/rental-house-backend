package com.codegym.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_id")
    private Rental rental;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id")
    private House house;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Builder.Default
    private Boolean isRead = false;

    public enum Type {
        RENTAL_REQUEST,      // Thông báo cho host khi có yêu cầu thuê mới
        RENTAL_APPROVED,     // Thông báo cho user khi yêu cầu được chấp nhận
        RENTAL_REJECTED,     // Thông báo cho user khi yêu cầu bị từ chối
        RENTAL_CANCELED,     // Thông báo cho host khi khách hủy thuê
        RENTAL_BOOKED,       // Thông báo cho host khi khách đặt thuê thành công
        REVIEW_LOW_RATING,   // Thông báo cho host khi có đánh giá 1-2 sao
        REVIEW_MEDIUM_RATING, // Thông báo cho host khi có đánh giá 3 sao
        REVIEW_HIGH_RATING,  // Thông báo cho host khi có đánh giá 4-5 sao
        HOUSE_DELETED,       // Thông báo cho host khi nhà bị xóa
        GENERAL             // Thông báo chung
    }
}
