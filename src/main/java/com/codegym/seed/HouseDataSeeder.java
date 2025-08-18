package com.codegym.seed;

import com.codegym.entity.House;
import com.codegym.entity.HouseImage;
import com.codegym.entity.Role;
import com.codegym.entity.RoleName;
import com.codegym.entity.User;
import com.codegym.repository.HouseImageRepository;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.RoleRepository;
import com.codegym.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class  HouseDataSeeder {

    @Bean
    public CommandLineRunner seedHouses(
            HouseRepository houseRepository,
            HouseImageRepository houseImageRepository,
            UserRepository userRepository,
            RoleRepository roleRepository) {
        return args -> {
            // Kiểm tra xem đã có nhà nào chưa
            if (houseRepository.count() > 0) {
                System.out.println("Houses already exist, skipping seed data.");
                return;
            }

            // Tìm user có role HOST để làm chủ nhà
            User host = userRepository.findByRole_Name(RoleName.HOST)
                    .stream()
                    .findFirst()
                    .orElseGet(() -> {
                        // Nếu không có HOST nào, tạo một HOST mới
                        Role hostRole = roleRepository.findByName(RoleName.HOST)
                                .orElseThrow(() -> new RuntimeException("HOST role not found"));
                        
                        User newHost = User.builder()
                                .username("host1")
                                .email("host1@example.com")
                                .phone("0987654321")
                                .password("$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi") // password
                                .role(hostRole)
                                .active(true)
                                .fullName("Nguyễn Văn Chủ Nhà")
                                .address("Hà Nội, Việt Nam")
                                .build();
                        return userRepository.save(newHost);
                    });

            if (host == null) {
                System.out.println("No host found, cannot create houses.");
                return;
            }
            
            System.out.println("Creating sample houses with host: " + host.getFullName());

            // Tạo nhà mẫu 1
            House house1 = House.builder()
                    .host(host)
                    .title("Căn hộ cao cấp tại Ba Đình")
                    .description("Căn hộ 2 phòng ngủ, 2 phòng tắm, đầy đủ tiện nghi, view đẹp, gần trung tâm.")
                    .address("123 Nguyễn Chí Thanh, Ba Đình, Hà Nội")
                    .price(15000000.0)
                    .area(80.0)
                    .latitude(21.0285)
                    .longitude(105.8542)
                    .status(House.Status.AVAILABLE)
                    .houseType(House.HouseType.APARTMENT)
                    .build();

            house1 = houseRepository.save(house1);

            // Tạo ảnh cho nhà 1
            List<String> images1 = Arrays.asList(
                    "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1560448075-bb485b067938?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1560448204-603b3fc33ddc?w=800&h=600&fit=crop"
            );

            for (String imageUrl : images1) {
                HouseImage houseImage = new HouseImage();
                houseImage.setHouse(house1);
                houseImage.setImageUrl(imageUrl);
                houseImageRepository.save(houseImage);
            }

            // Tạo nhà mẫu 2
            House house2 = House.builder()
                    .host(host)
                    .title("Nhà phố 3 tầng tại Cầu Giấy")
                    .description("Nhà phố 3 tầng, 4 phòng ngủ, 3 phòng tắm, có gara xe, sân vườn nhỏ.")
                    .address("456 Xuân Thủy, Cầu Giấy, Hà Nội")
                    .price(25000000.0)
                    .area(120.0)
                    .latitude(21.0368)
                    .longitude(105.7826)
                    .status(House.Status.AVAILABLE)
                    .houseType(House.HouseType.TOWNHOUSE)
                    .build();

            house2 = houseRepository.save(house2);

            // Tạo ảnh cho nhà 2
            List<String> images2 = Arrays.asList(
                    "https://images.unsplash.com/photo-1570129477492-45c003edd2be?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1564013799919-ab600027ffc6?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1564013799919-ab600027ffc6?w=800&h=600&fit=crop"
            );

            for (String imageUrl : images2) {
                HouseImage houseImage = new HouseImage();
                houseImage.setHouse(house2);
                houseImage.setImageUrl(imageUrl);
                houseImageRepository.save(houseImage);
            }

            // Tạo nhà mẫu 3
            House house3 = House.builder()
                    .host(host)
                    .title("Biệt thự sang trọng tại Tây Hồ")
                    .description("Biệt thự 5 phòng ngủ, 4 phòng tắm, hồ bơi riêng, view hồ Tây tuyệt đẹp.")
                    .address("789 Lạc Long Quân, Tây Hồ, Hà Nội")
                    .price(50000000.0)
                    .area(200.0)
                    .latitude(21.0455)
                    .longitude(105.8234)
                    .status(House.Status.AVAILABLE)
                    .houseType(House.HouseType.VILLA)
                    .build();

            house3 = houseRepository.save(house3);

            // Tạo ảnh cho nhà 3
            List<String> images3 = Arrays.asList(
                    "https://images.unsplash.com/photo-1613490493576-7fde63acd811?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?w=800&h=600&fit=crop"
            );

            for (String imageUrl : images3) {
                HouseImage houseImage = new HouseImage();
                houseImage.setHouse(house3);
                houseImage.setImageUrl(imageUrl);
                houseImageRepository.save(houseImage);
            }

            // Tạo nhà mẫu 4
            House house4 = House.builder()
                    .host(host)
                    .title("Nhà trọ sinh viên tại Đống Đa")
                    .description("Nhà trọ sạch sẽ, an ninh, gần trường đại học, phù hợp cho sinh viên.")
                    .address("321 Tôn Đức Thắng, Đống Đa, Hà Nội")
                    .price(3000000.0)
                    .area(25.0)
                    .latitude(21.0198)
                    .longitude(105.8342)
                    .status(House.Status.AVAILABLE)
                    .houseType(House.HouseType.BOARDING_HOUSE)
                    .build();

            house4 = houseRepository.save(house4);

            // Tạo ảnh cho nhà 4
            List<String> images4 = Arrays.asList(
                    "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=800&h=600&fit=crop",
                    "https://images.unsplash.com/photo-1560448075-bb485b067938?w=800&h=600&fit=crop"
            );

            for (String imageUrl : images4) {
                HouseImage houseImage = new HouseImage();
                houseImage.setHouse(house4);
                houseImage.setImageUrl(imageUrl);
                houseImageRepository.save(houseImage);
            }

            System.out.println("Created " + houseRepository.count() + " sample houses successfully!");
        };
    }
} 