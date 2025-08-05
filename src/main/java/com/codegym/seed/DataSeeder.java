package com.codegym.seed;

import com.codegym.entity.Role;
import com.codegym.entity.User;
import com.codegym.repository.RoleRepository;
import com.codegym.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.codegym.constants.RoleConstants.ADMIN;

@Configuration
public class DataSeeder {
    @Bean
    public CommandLineRunner seedAll(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Seed roles
            for (String roleName : new String[]{"ADMIN", "SALON_OWNER", "USER"}) {
                // Chỉ tạo role mới nếu nó chưa tồn tại
                if (roleRepository.findByName(roleName).isEmpty()) {
                    roleRepository.save(new Role(roleName));
                }
            }

            // Seed admin user
            if (userRepository.findByUsername("admin").isEmpty()) {
                // Tìm role ADMIN, nếu không thấy sẽ báo lỗi (đúng vì admin phải có role)
                Role adminRole = roleRepository.findByName(ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: ADMIN role not found."));

                // Dùng Builder để code gọn gàng và dễ đọc hơn
                User admin = User.builder()
                        .username("admin")
                        .email("admin@rentalhouse.com") // Bổ sung email
                        .phone("0000000000") // Bổ sung số điện thoại
                        .password(passwordEncoder.encode("admin123")) // Mật khẩu đã mã hóa
                        .role(adminRole) // Gán role
                        .active(true) // Set tài khoản active
                        .avatarUrl("https://example.com/default-avatar.png") // Cung cấp avatar mặc định
                        .facebookAccountId(0) // Cung cấp giá trị mặc định
                        .googleAccountId(0)   // Cung cấp giá trị mặc định
                        .build();

                userRepository.save(admin);
                System.out.println("Admin user created successfully!");
            }
        };
    }
}