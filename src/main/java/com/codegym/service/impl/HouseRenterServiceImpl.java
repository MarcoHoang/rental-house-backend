package com.codegym.service.impl;

import com.codegym.dto.response.HouseDTO;
import com.codegym.dto.response.HouseRenterDTO;
import com.codegym.entity.House;
import com.codegym.entity.HouseImage;
import com.codegym.entity.HouseRenter;
import com.codegym.entity.User;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.HouseRenterRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.HouseRenterService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseRenterServiceImpl implements HouseRenterService {

    @Autowired
    private HouseRenterRepository houseRenterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HouseRepository houseRepository; // Thêm để lấy danh sách nhà

    // ---- CÁC PHƯƠNG THỨC MAPPER ----

    private HouseRenterDTO toDTO(HouseRenter houseRenter) {
        HouseRenterDTO dto = new HouseRenterDTO();
        dto.setId(houseRenter.getId());
        dto.setNationalId(houseRenter.getNationalId());
        dto.setProofOfOwnershipUrl(houseRenter.getProofOfOwnershipUrl());
        dto.setAddress(houseRenter.getAddress());
        dto.setApprovedDate(houseRenter.getApprovedDate());
        dto.setApproved(houseRenter.getApprovedDate() != null);

        if (houseRenter.getUser() != null) {
            User user = houseRenter.getUser();
            dto.setFullName(user.getFullName());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setAvatar(user.getAvatarUrl());
        }

        return dto;
    }

    // Helper để cập nhật entity đã tồn tại từ DTO, tránh tạo mới không cần thiết
    private void updateEntityFromDTO(HouseRenter houseRenter, HouseRenterDTO dto, User user) {
        houseRenter.setUser(user);
        houseRenter.setNationalId(dto.getNationalId());
        houseRenter.setProofOfOwnershipUrl(dto.getProofOfOwnershipUrl());
        houseRenter.setAddress(dto.getAddress());
        houseRenter.setApprovedDate(dto.getApprovedDate());
    }

    // ---- TRIỂN KHAI SERVICE ----

    @Override
    @Transactional(readOnly = true)
    public List<HouseRenterDTO> getAllHouseRenters() {
        return houseRenterRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HouseRenterDTO getHouseRenterById(Long id) {
        return houseRenterRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy chủ nhà với ID: " + id));
    }

    @Override
    @Transactional
    public HouseRenterDTO createHouseRenter(HouseRenterDTO dto) {
        // 1. Kiểm tra xem User có tồn tại không
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng với ID: " + dto.getId() + " để đăng ký làm chủ nhà."));

        // 2. Kiểm tra xem người dùng này đã là chủ nhà chưa
        if (houseRenterRepository.existsById(dto.getId())) {
            throw new IllegalArgumentException("Người dùng với ID " + dto.getId() + " đã được đăng ký làm chủ nhà.");
        }

        // 3. Tạo mới HouseRenter và liên kết
        HouseRenter houseRenter = new HouseRenter();
        houseRenter.setId(user.getId()); // ID của HouseRenter phải trùng với ID của User
        updateEntityFromDTO(houseRenter, dto, user);

        HouseRenter savedHouseRenter = houseRenterRepository.save(houseRenter);
        return toDTO(savedHouseRenter);
    }

    @Override
    @Transactional
    public HouseRenterDTO updateHouseRenter(Long id, HouseRenterDTO dto) {
        // 1. Lấy HouseRenter đã tồn tại từ DB
        HouseRenter existingHouseRenter = houseRenterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy chủ nhà với ID: " + id + " để cập nhật."));

        // 2. Cập nhật các trường từ DTO vào entity đã tồn tại
        updateEntityFromDTO(existingHouseRenter, dto, existingHouseRenter.getUser());

        HouseRenter updatedHouseRenter = houseRenterRepository.save(existingHouseRenter);
        return toDTO(updatedHouseRenter);
    }

    @Override
    @Transactional
    public void deleteHouseRenter(Long id) {
        if (!houseRenterRepository.existsById(id)) {
            throw new EntityNotFoundException("Không thể xóa. Chủ nhà với ID: " + id + " không tồn tại.");
        }
        // Cân nhắc: Khi xóa chủ nhà, có nên xóa cả các nhà của họ? (Logic nghiệp vụ)
        houseRenterRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void lockHouseRenter(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng với ID: " + id + " để khóa."));
        user.setActive(false); // Giả sử bạn có trường `isActive` trên User entity
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unlockHouseRenter(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng với ID: " + id + " để mở khóa."));
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HouseDTO> getHouseRenterHouses(Long id) {
        // 1. Kiểm tra chủ nhà có tồn tại không
        if (!houseRenterRepository.existsById(id)) {
            throw new EntityNotFoundException("Không tìm thấy chủ nhà với ID: " + id);
        }

        // 2. Lấy danh sách nhà theo ID chủ nhà
        // (Yêu cầu phải có phương thức này trong HouseRepository)
        List<House> houses = houseRepository.findByHouseRenterId(id);

        // 3. Chuyển đổi List<House> sang List<HouseDTO>
        return houses.stream().map(this::toHouseDTO).collect(Collectors.toList());
    }

    // Phương thức helper để map House sang HouseDTO (tốt nhất nên có một class Mapper riêng)
    private HouseDTO toHouseDTO(House house) {
        return HouseDTO.builder()
                .id(house.getId())
                .houseRenterId(house.getHouseRenter().getId())
                .houseRenterName(house.getHouseRenter().getUsername())
                .title(house.getTitle())
                .address(house.getAddress())
                .price(house.getPrice())
                .imageUrls(house.getImages() != null ?
                        house.getImages().stream().map(HouseImage::getImageUrl).collect(Collectors.toList())
                        : List.of())
                //... các trường khác của HouseDTO
                .build();
    }
}