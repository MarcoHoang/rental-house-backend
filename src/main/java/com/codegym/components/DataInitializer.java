package com.codegym.components;

import com.codegym.entity.Role;
import com.codegym.entity.RoleName;
import com.codegym.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            for (RoleName roleName : RoleName.values()) {
                boolean exists = roleRepository.findByName(roleName).isPresent();
                if (!exists) {
                    Role role = new Role(roleName);
                    roleRepository.save(role);
                    System.out.println("Inserted Role: " + roleName.name());
                }
            }
        };
    }
}
