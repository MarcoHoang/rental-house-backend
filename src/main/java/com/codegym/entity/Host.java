package com.codegym.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "hosts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Host extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String nationalId;
    private String proofOfOwnershipUrl;
    private String address;
    private LocalDateTime approvedDate;

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        this.approvedDate = LocalDateTime.now();
    }
}

