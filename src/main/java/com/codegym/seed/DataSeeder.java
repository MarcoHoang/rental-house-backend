package com.codegym.seed;

import com.codegym.entity.Role;
import com.codegym.entity.RoleName;
import com.codegym.entity.User;
import com.codegym.repository.RoleRepository;
import com.codegym.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {
    @Bean
    public CommandLineRunner seedAll(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Seed roles
            for (RoleName roleName : RoleName.values()) {
                if (roleRepository.findByName(roleName).isEmpty()) {
                    roleRepository.save(new Role(roleName));
                }
            }

            // Seed admin user
            if (userRepository.findByUsername("admin").isEmpty()) {
                Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                        .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

                User admin = User.builder()
                        .username("admin")
                        .email("admin@rentalhouse.com")
                        .phone("0000000000")
                        .password(passwordEncoder.encode("admin123"))
                        .role(adminRole)
                        .active(true)
                        .avatarUrl("https://example.com/default-avatar.png")
                        .facebookAccountId(null)
                        .googleAccountId(null)
                        .fullName("Administrator")
                        .address("System Address")
                        .build();

                userRepository.save(admin);
                System.out.println("Admin user created successfully!");
            }

        };
    }
}