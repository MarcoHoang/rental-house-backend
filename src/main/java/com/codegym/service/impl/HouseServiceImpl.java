package com.codegym.service.impl;

import com.codegym.dto.request.HouseRequest;
import com.codegym.dto.response.HouseDTO;
import com.codegym.entity.House;
import com.codegym.entity.HouseImage;
import com.codegym.entity.User;
import com.codegym.entity.RoleName;
import com.codegym.exception.AppException;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.HouseImageRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.HouseService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.codegym.service.GeocodingService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final HouseImageRepository houseImageRepository;
    private final UserRepository userRepository;
    private final GeocodingService geocodingService;

    private House findHouseByIdOrThrow(Long id) {
        return houseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.HOUSE_NOT_FOUND, id));
    }

    private User findHostByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.HOST_NOT_FOUND, id));
    }

    private HouseDTO toDTO(House house) {
        return HouseDTO.builder()
                .id(house.getId())
                .hostId(house.getHost().getId())
                .hostName(house.getHost().getFullName())
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
                        : List.of())
                .createdAt(house.getCreatedAt())
                .updatedAt(house.getUpdatedAt())
                .build();
    }

    private void updateEntityFromDTO(House house, HouseDTO dto, User host) {
        house.setHost(host);
        house.setTitle(dto.getTitle());
        house.setDescription(dto.getDescription());
        house.setAddress(dto.getAddress());
        house.setPrice(dto.getPrice());
        house.setArea(dto.getArea());
        house.setLatitude(dto.getLatitude());
        house.setLongitude(dto.getLongitude());
        house.setStatus(dto.getStatus());
        house.setHouseType(dto.getHouseType());
    }

    private void updateEntityFromRequest(House house, HouseRequest request, User host) {
        house.setHost(host);
        house.setTitle(request.getTitle());
        house.setDescription(request.getDescription());
        house.setAddress(request.getAddress());
        house.setPrice(request.getPrice());
        house.setArea(request.getArea());
        house.setLatitude(request.getLatitude());
        house.setLongitude(request.getLongitude());
        house.setStatus(House.Status.AVAILABLE); // Mặc định là AVAILABLE khi tạo/sửa
        house.setHouseType(request.getHouseType());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HouseDTO> getAllHouses() {
        return houseRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HouseDTO getHouseById(Long id) {
        House house = findHouseByIdOrThrow(id);
        return toDTO(house);
    }

    @Override
    @Transactional
    public HouseDTO createHouse(HouseRequest request) {
        // Lấy host hiện tại từ authentication
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUsername));
        
        if (!currentUser.getRole().getName().equals(RoleName.HOST)) {
            throw new AppException(StatusCode.FORBIDDEN_ACTION, "Chỉ chủ nhà mới được tạo nhà");
        }

        House house = new House();
        updateEntityFromRequest(house, request, currentUser);

        // Xử lý geocoding một cách graceful - nếu thất bại thì vẫn cho phép tạo nhà
        if (house.getLatitude() == null || house.getLongitude() == null) {
            try {
                double[] latLng = geocodingService.getLatLngFromAddress(house.getAddress());
                house.setLatitude(latLng[0]);
                house.setLongitude(latLng[1]);
            } catch (Exception e) {
                // Log lỗi geocoding nhưng không throw exception
                // Cho phép tạo nhà với địa chỉ mà không cần tọa độ
                System.err.println("Geocoding failed for address: " + house.getAddress() + ". Error: " + e.getMessage());
                
                // Đặt tọa độ về null để đánh dấu là không có tọa độ
                house.setLatitude(null);
                house.setLongitude(null);
                
                // Có thể log thêm thông tin để debug
                System.err.println("House will be created without coordinates. Address: " + house.getAddress());
            }
        }

        house.setId(null);
        House savedHouse = houseRepository.save(house);

        // Lưu ảnh nếu có
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            saveHouseImages(savedHouse, request.getImageUrls());
        }

        return toDTO(savedHouse);
    }

    @Override
    @Transactional
    public HouseDTO updateHouse(Long id, HouseRequest request) {
        House existingHouse = findHouseByIdOrThrow(id);
        
        // Kiểm tra ownership - chỉ chủ nhà mới được sửa nhà của mình, admin không được sửa
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUsername));
        
        if (!currentUser.getRole().getName().equals(RoleName.HOST) || 
            !existingHouse.getHost().getId().equals(currentUser.getId())) {
            throw new AppException(StatusCode.FORBIDDEN_ACTION, "Bạn không có quyền sửa nhà này");
        }

        boolean addressChanged = !existingHouse.getAddress().equals(request.getAddress());

        updateEntityFromRequest(existingHouse, request, existingHouse.getHost());

        // Xử lý geocoding một cách graceful - nếu thất bại thì vẫn cho phép lưu nhà
        if (addressChanged || existingHouse.getLatitude() == null || existingHouse.getLongitude() == null) {
            try {
                double[] latLng = geocodingService.getLatLngFromAddress(existingHouse.getAddress());
                existingHouse.setLatitude(latLng[0]);
                existingHouse.setLongitude(latLng[1]);
            } catch (Exception e) {
                // Log lỗi geocoding nhưng không throw exception
                // Cho phép lưu nhà với địa chỉ mà không cần tọa độ
                System.err.println("Geocoding failed for address: " + existingHouse.getAddress() + ". Error: " + e.getMessage());
                
                // Đặt tọa độ về null để đánh dấu là không có tọa độ
                existingHouse.setLatitude(null);
                existingHouse.setLongitude(null);
                
                // Có thể log thêm thông tin để debug
                System.err.println("House will be saved without coordinates. Address: " + existingHouse.getAddress());
            }
        }

        // Cập nhật ảnh nếu có
        if (request.getImageUrls() != null) {
            updateHouseImages(existingHouse, request.getImageUrls());
        }

        House updatedHouse = houseRepository.save(existingHouse);
        return toDTO(updatedHouse);
    }

    @Override
    @Transactional
    public void deleteHouse(Long id) {
        House house = findHouseByIdOrThrow(id);
        
        // Kiểm tra ownership và trạng thái nhà
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUsername));
        
        // Admin có thể xóa bất kỳ nhà nào, nhưng không được xóa nhà đang được thuê
        if (currentUser.getRole().getName().equals(RoleName.ADMIN)) {
            if (house.getStatus() == House.Status.RENTED) {
                throw new AppException(StatusCode.FORBIDDEN_ACTION, "Không thể xóa nhà đang được thuê");
            }
        } 
        // Host chỉ được xóa nhà của mình
        else if (currentUser.getRole().getName().equals(RoleName.HOST)) {
            if (!house.getHost().getId().equals(currentUser.getId())) {
                throw new AppException(StatusCode.FORBIDDEN_ACTION, "Bạn không có quyền xóa nhà này");
            }
            if (house.getStatus() == House.Status.RENTED) {
                throw new AppException(StatusCode.FORBIDDEN_ACTION, "Không thể xóa nhà đang được thuê");
            }
        } else {
            throw new AppException(StatusCode.FORBIDDEN_ACTION, "Bạn không có quyền xóa nhà");
        }
        
        houseRepository.delete(house);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HouseDTO> searchHouses(String keyword) {
        return houseRepository.findAll().stream()
                .filter(h -> keyword == null || h.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HouseDTO> getTopHouses() {
        return houseRepository.findAll().stream().limit(5).map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public HouseDTO updateHouseStatus(Long id, String status) {
        House house = findHouseByIdOrThrow(id);
        
        // Kiểm tra ownership - chỉ chủ nhà mới được thay đổi trạng thái nhà của mình
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUsername));
        
        if (!currentUser.getRole().getName().equals(RoleName.HOST) || 
            !house.getHost().getId().equals(currentUser.getId())) {
            throw new AppException(StatusCode.FORBIDDEN_ACTION, "Bạn không có quyền thay đổi trạng thái nhà này");
        }

        boolean isValidStatus = Arrays.stream(House.Status.values())
                .anyMatch(s -> s.name().equalsIgnoreCase(status));

        if (!isValidStatus) {
            throw new AppException(StatusCode.INVALID_HOUSE_STATUS, status);
        }

        house.setStatus(House.Status.valueOf(status.toUpperCase()));
        return toDTO(houseRepository.save(house));
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getHouseImages(Long id) {
        House house = findHouseByIdOrThrow(id);
        return house.getImages() != null
                ? house.getImages().stream().map(HouseImage::getImageUrl).collect(Collectors.toList())
                : List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HouseDTO> getHousesByCurrentHost() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUsername));

        if (!currentUser.getRole().getName().equals(RoleName.HOST)) {
            throw new AppException(StatusCode.UNAUTHORIZED, "Người dùng không phải là chủ nhà");
        }

        return houseRepository.findByHost(currentUser).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lưu danh sách ảnh cho nhà
     */
    private void saveHouseImages(House house, List<String> imageUrls) {
        for (int i = 0; i < imageUrls.size(); i++) {
            HouseImage houseImage = HouseImage.builder()
                    .house(house)
                    .imageUrl(imageUrls.get(i))
                    .sortOrder(i + 1)
                    .build();
            houseImageRepository.save(houseImage);
        }
    }

    /**
     * Cập nhật ảnh cho nhà (xóa ảnh cũ, thêm ảnh mới)
     */
    private void updateHouseImages(House house, List<String> newImageUrls) {
        // Xóa tất cả ảnh cũ
        houseImageRepository.deleteByHouse(house);
        
        // Thêm ảnh mới
        if (!newImageUrls.isEmpty()) {
            saveHouseImages(house, newImageUrls);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<HouseDTO> getAllHousesForAdmin(org.springframework.data.domain.Pageable pageable) {
        org.springframework.data.domain.Page<House> housePage = houseRepository.findAll(pageable);
        return housePage.map(this::toDTO);
    }
}