package com.codegym.controller.api;

import com.codegym.entity.Role;
import com.codegym.entity.RoleName;
import com.codegym.entity.User;
import com.codegym.repository.RoleRepository;
import com.codegym.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestDataController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/insert-users")
    public ResponseEntity<String> insertTestUsers() {
        try {
            // Create roles if they don't exist
            createRolesIfNotExist();

            // Insert test users
            insertUsers();

            return ResponseEntity.ok("Test users inserted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/test-image")
    public ResponseEntity<String> testImage() {
        return ResponseEntity.ok("Image test endpoint is working!");
    }

    private void createRolesIfNotExist() {
        List<RoleName> roleNames = Arrays.asList(RoleName.ADMIN, RoleName.HOST, RoleName.USER);
        
        for (RoleName roleName : roleNames) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }
    }

    private void insertUsers() {
        // Check if users already exist
        if (userRepository.count() > 0) {
            return; // Users already exist
        }

        // Get roles
        Role adminRole = roleRepository.findByName(RoleName.ADMIN).orElseThrow();
        Role hostRole = roleRepository.findByName(RoleName.HOST).orElseThrow();
        Role userRole = roleRepository.findByName(RoleName.USER).orElseThrow();

        // Password for all users: 123456
        String encodedPassword = passwordEncoder.encode("123456");

        // Admin users
        createUser("admin1", "admin1@rentalhouse.com", "0901234567", encodedPassword, 
                  "Nguyễn Văn Admin", "123 Đường ABC, Quận 1, TP.HCM", 
                  LocalDate.of(1985, 3, 15), adminRole);

        createUser("admin2", "admin2@rentalhouse.com", "0901234568", encodedPassword, 
                  "Trần Thị Quản Lý", "456 Đường DEF, Quận 2, TP.HCM", 
                  LocalDate.of(1990, 7, 22), adminRole);

        // Host users
        createUser("host1", "host1@rentalhouse.com", "0901234570", encodedPassword, 
                  "Phạm Văn Chủ Nhà", "321 Đường JKL, Quận 1, TP.HCM", 
                  LocalDate.of(1982, 5, 20), hostRole);

        createUser("host2", "host2@rentalhouse.com", "0901234571", encodedPassword, 
                  "Hoàng Thị Chủ Nhà", "654 Đường MNO, Quận 2, TP.HCM", 
                  LocalDate.of(1987, 9, 12), hostRole);

        // Regular users
        createUser("user1", "user1@rentalhouse.com", "0901234580", encodedPassword, 
                  "Lê Văn Khách", "357 Đường NOP, Quận 11, TP.HCM", 
                  LocalDate.of(1995, 3, 25), userRole);

        createUser("user2", "user2@rentalhouse.com", "0901234581", encodedPassword, 
                  "Phan Thị Khách", "468 Đường QRS, Quận 12, TP.HCM", 
                  LocalDate.of(1993, 7, 16), userRole);

        createUser("user3", "user3@rentalhouse.com", "0901234582", encodedPassword, 
                  "Võ Văn Khách", "579 Đường TUV, Quận Bình Tân, TP.HCM", 
                  LocalDate.of(1997, 11, 9), userRole);

        createUser("user4", "user4@rentalhouse.com", "0901234583", encodedPassword, 
                  "Trần Thị Khách", "680 Đường WXY, Quận Tân Bình, TP.HCM", 
                  LocalDate.of(1994, 5, 22), userRole);

        createUser("user5", "user5@rentalhouse.com", "0901234584", encodedPassword, 
                  "Nguyễn Văn Khách", "791 Đường ZAB, Quận Bình Thạnh, TP.HCM", 
                  LocalDate.of(1996, 9, 3), userRole);

        createUser("user6", "user6@rentalhouse.com", "0901234585", encodedPassword, 
                  "Hoàng Thị Khách", "802 Đường CDE, Quận Gò Vấp, TP.HCM", 
                  LocalDate.of(1992, 1, 18), userRole);

        createUser("user7", "user7@rentalhouse.com", "0901234586", encodedPassword, 
                  "Đặng Văn Khách", "913 Đường FGH, Quận Phú Nhuận, TP.HCM", 
                  LocalDate.of(1998, 4, 12), userRole);

        createUser("user8", "user8@rentalhouse.com", "0901234587", encodedPassword, 
                  "Bùi Thị Khách", "024 Đường IJK, Quận 1, TP.HCM", 
                  LocalDate.of(1991, 8, 27), userRole);

        createUser("user9", "user9@rentalhouse.com", "0901234588", encodedPassword, 
                  "Lý Văn Khách", "135 Đường LMN, Quận 2, TP.HCM", 
                  LocalDate.of(1995, 12, 5), userRole);

        createUser("user10", "user10@rentalhouse.com", "0901234589", encodedPassword, 
                  "Trịnh Thị Khách", "246 Đường OPQ, Quận 3, TP.HCM", 
                  LocalDate.of(1993, 6, 19), userRole);
    }

    private void createUser(String username, String email, String phone, String password, 
                           String fullName, String address, LocalDate birthDate, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);
        user.setActive(true);
        user.setAvatarUrl("/images/default-avatar.png");
        user.setFullName(fullName);
        user.setAddress(address);
        user.setBirthDate(birthDate);
        user.setRole(role);
        
        userRepository.save(user);
    }
}
