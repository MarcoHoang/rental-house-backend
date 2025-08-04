package com.codegym.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    private Long id; // dùng chung ID với User

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String avatar = "/images/default-avatar.png"; // Avatar mặc định

    @NotBlank(message = "Họ và tên không được để trống")
    @Pattern(regexp = "^[\\p{L} ]+$", message = "Họ và tên không chứa ký tự đặc biệt")
    private String fullName;

    @Pattern(regexp = "^[\\p{L}0-9\\s,.-]*$", message = "Địa chỉ không hợp lệ")
    private String address;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{10,11}$", message = "Số điện thoại không hợp lệ")
    private String phone;
}
