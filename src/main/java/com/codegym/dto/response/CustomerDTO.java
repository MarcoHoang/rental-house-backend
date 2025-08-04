package com.codegym.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Long id;                 // ID dùng chung với User
    private String fullName;         // Họ và tên
    private String address;          // Địa chỉ
    private String phone;            // Số điện thoại
    private String avatar;           // Đường dẫn ảnh đại diện
    private String username;         // Tên đăng nhập của user
    private String email;            // Email của user
}
