package com.codegym.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental extends BaseEntity {

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
    private Status status;

    private Double totalPrice;

    @Column(name = "guest_count")
    private Integer guestCount;

    @Column(name = "message_to_host", length = 1000)
    private String messageToHost;

    @Column(name = "reject_reason", length = 500)
    private String rejectReason;

    @Column(name = "cancel_reason", length = 500)
    private String cancelReason;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rejected_by")
    private User rejectedBy;

    public enum Status {
        PENDING,      // Chờ host duyệt
        APPROVED,     // Host đã đồng ý
        REJECTED,     // Host từ chối
        SCHEDULED,    // Đã confirm (sau khi approved)
        CHECKED_IN,   // Đã nhận phòng
        CHECKED_OUT,  // Đã trả phòng
        CANCELED      // Đã hủy
    }
}
