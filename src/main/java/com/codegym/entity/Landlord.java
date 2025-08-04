package com.codegym.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "landlords")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Landlord {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String avatar = "/images/default-avatar.png";

    @NotBlank(message = "Họ và tên không được để trống")
    @Pattern(regexp = "^[\\p{L} ]+$", message = "Họ và tên không chứa ký tự đặc biệt")
    private String fullName;

    @Pattern(regexp = "^[\\p{L}0-9\\s,.-]*$", message = "Địa chỉ không hợp lệ")
    private String address;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{10,11}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    private boolean approved = false; // Được admin duyệt hay chưa
}
