package com.codegym.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "house_renters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HouseRenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String nationalId; // CCCD/CMND
    private String proofOfOwnershipUrl; // link ảnh/sao y sổ hồng, giấy tờ
    private String address; // địa chỉ chính thức
    private LocalDateTime approvedDate;

    @PrePersist
    protected void onCreate() {
        this.approvedDate = LocalDateTime.now();
    }
}

