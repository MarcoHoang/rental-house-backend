package com.codegym.service.impl;

import com.codegym.dto.response.HouseDTO;
import com.codegym.entity.House;
import com.codegym.entity.HouseImage;
import com.codegym.entity.User;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.HouseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseServiceImpl implements HouseService {

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private UserRepository userRepository;

    private HouseDTO toDTO(House house) {
        return HouseDTO.builder()
                .id(house.getId())
                .houseRenterId(house.getHouseRenter().getId())
                .houseRenterName(house.getHouseRenter().getUsername())
                .title(house.getTitle())
                .description(house.getDescription())
                .address(house.getAddress())
                .price(house.getPrice())
                .area(house.getArea())
                .latitude(house.getLatitude())
                .longitude(house.getLongitude())
                .status(house.getStatus())
                .houseType(house.getHouseType())
                .imageUrls(house.getImages() != null
                        ? house.getImages().stream().map(HouseImage::getImageUrl).collect(Collectors.toList())
                        : List.of()) // Trả về list rỗng thay vì null
                .createdAt(house.getCreatedAt())
                .updatedAt(house.getUpdatedAt())
                .build();
    }

    private void updateEntityFromDTO(House house, HouseDTO dto, User landlord) {
        house.setHouseRenter(landlord);
        house.setTitle(dto.getTitle());
        house.setDescription(dto.getDescription());
        house.setAddress(dto.getAddress());
        house.setPrice(dto.getPrice());
        house.setArea(dto.getArea());
        house.setLatitude(dto.getLatitude());
        house.setLongitude(dto.getLongitude());
        house.setStatus(dto.getStatus());
        house.setHouseType(dto.getHouseType());
        // Logic cập nhật ảnh có thể phức tạp hơn, cần xử lý riêng
    }


    @Override
    @Transactional(readOnly = true)
    public List<HouseDTO> getAllHouses() {
        return houseRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HouseDTO getHouseById(Long id) {
        return houseRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nhà với ID: " + id));
    }

    @Override
    @Transactional
    public HouseDTO createHouse(HouseDTO dto) {
        User landlord = userRepository.findById(dto.getHouseRenterId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy chủ nhà với ID: " + dto.getHouseRenterId()));

        House house = new House();
        // Dùng phương thức helper để gán giá trị, đảm bảo logic nhất quán
        updateEntityFromDTO(house, dto, landlord);
        // Khi tạo mới, id phải là null để JPA biết đây là INSERT
        house.setId(null);

        House savedHouse = houseRepository.save(house);
        return toDTO(savedHouse);
    }

    @Override
    @Transactional
    public HouseDTO updateHouse(Long id, HouseDTO dto) {
        // 1. Tìm nhà cần cập nhật
        House existingHouse = houseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nhà với ID: " + id + " để cập nhật."));

        // 2. Tìm chủ nhà (nếu có thay đổi)
        User landlord = userRepository.findById(dto.getHouseRenterId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy chủ nhà với ID: " + dto.getHouseRenterId()));

        // 3. Cập nhật các trường từ DTO vào entity đã tồn tại
        updateEntityFromDTO(existingHouse, dto, landlord);

        House updatedHouse = houseRepository.save(existingHouse);
        return toDTO(updatedHouse);
    }

    @Override
    @Transactional
    public void deleteHouse(Long id) {
        if (!houseRepository.existsById(id)) {
            throw new EntityNotFoundException("Không thể xóa. Nhà với ID: " + id + " không tồn tại.");
        }
        houseRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HouseDTO> searchHouses(String keyword) {
        // GỢI Ý: Để tối ưu hiệu năng, nên viết một query riêng trong HouseRepository
        // thay vì tải tất cả record lên rồi filter trong bộ nhớ.
        // Ví dụ: @Query("SELECT h FROM House h WHERE lower(h.title) LIKE lower(concat('%', :keyword, '%'))")
        return houseRepository.findAll().stream()
                .filter(h -> keyword == null || h.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HouseDTO> getTopHouses() {
        // GỢI Ý: Logic này nên được xử lý ở tầng Repository với Pageable hoặc query native
        // để đạt hiệu suất tốt nhất, thay vì lấy tất cả rồi giới hạn.
        // Ví dụ: Pageable pageable = PageRequest.of(0, 5, Sort.by("rentalCount").descending());
        // houseRepository.findAll(pageable).getContent();
        return houseRepository.findAll().stream().limit(5).map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public HouseDTO updateHouseStatus(Long id, String status) {
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nhà với ID: " + id));

        try {
            house.setStatus(House.Status.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            // Ném lỗi này sẽ được GlobalExceptionHandler bắt và trả về 400 BAD REQUEST
            throw new IllegalArgumentException("Trạng thái '" + status + "' không hợp lệ.");
        }

        return toDTO(houseRepository.save(house));
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getHouseImages(Long id) {
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nhà với ID: " + id));

        return house.getImages() != null
                ? house.getImages().stream().map(HouseImage::getImageUrl).collect(Collectors.toList())
                : List.of();
    }
}