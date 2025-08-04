package com.codegym.components;


import com.codegym.entity.Role;
import com.codegym.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            List<String> defaultRoles = Arrays.asList("ADMIN", "USER");

            defaultRoles.forEach(roleName -> {
                boolean exists = roleRepository.findByName(roleName).isPresent();
                if (!exists) {
                    Role role = new Role();
                    role.setName(roleName);
                    roleRepository.save(role);
                    System.out.println("Inserted Role: " + roleName);
                }
            });
        };
    }
}
