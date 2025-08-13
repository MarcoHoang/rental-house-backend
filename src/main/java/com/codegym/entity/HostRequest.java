package com.codegym.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "host_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "national_id")
    private String nationalId;

    @Column(name = "proof_of_ownership_url")
    private String proofOfOwnershipUrl;

    private String reason;

    private LocalDateTime requestDate;
    private LocalDateTime processedDate;

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }

    @PrePersist
    protected void onCreate() {
        this.requestDate = LocalDateTime.now();
    }
}


