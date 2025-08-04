package com.codegym.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;  // Không được thay đổi sau khi tạo

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // e.g. "ROLE_USER", "ROLE_LANDLORD", "ROLE_ADMIN"

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Customer customer;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Landlord landlord;

    private boolean enabled = true;
}
