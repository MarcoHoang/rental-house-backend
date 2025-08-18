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

    @Builder.Default
    private Boolean isRead = false;

    public enum Type {
        RENTAL_CREATED,
        RENTAL_CANCELED,
        REVIEW_RECEIVED,
        LANDLORD_REQUEST_APPROVED,
        LANDLORD_REQUEST_REJECTED,
        HOUSE_DELETED,
        GENERAL
    }
}
