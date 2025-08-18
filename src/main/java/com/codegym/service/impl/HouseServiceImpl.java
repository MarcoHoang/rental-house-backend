package com.codegym.service.impl;

import com.codegym.dto.request.HouseRequest;
import com.codegym.dto.response.HouseDTO;
import com.codegym.entity.House;
import com.codegym.entity.HouseImage;
import com.codegym.entity.User;
import com.codegym.entity.RoleName;
import com.codegym.entity.Notification;
import com.codegym.exception.AppException;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.HouseImageRepository;
import com.codegym.repository.UserRepository;
import com.codegym.repository.NotificationRepository;
import com.codegym.repository.FavoriteRepository;
import com.codegym.service.HouseService;
import com.codegym.service.NotificationService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.codegym.service.GeocodingService;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final HouseImageRepository houseImageRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final FavoriteRepository favoriteRepository;
    private final GeocodingService geocodingService;
    private final NotificationService notificationService;

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
                .hostId(house.getHost() != null ? house.getHost().getId() : null)
                .hostName(house.getHost() != null ? house.getHost().getFullName() : null)
                .hostPhone(house.getHost() != null ? house.getHost().getPhone() : null) // Lấy số điện thoại chủ nhà
                .hostAvatar(house.getHost() != null ? house.getHost().getAvatarUrl() : null) // Lấy avatar chủ nhà
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
                .favoriteCount(favoriteRepository.countByHouseId(house.getId()))
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
        house.setStatus(request.getStatus()); // Sử dụng trạng thái từ request
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

        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            saveHouseImages(savedHouse, request.getImageUrls());
        }

        return toDTO(savedHouse);
    }

    @Override
    @Transactional
    public HouseDTO updateHouse(Long id, HouseRequest request) {
        House existingHouse = findHouseByIdOrThrow(id);
        
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

        if (request.getImageUrls() != null) {
            updateHouseImages(existingHouse, request.getImageUrls());
        }

        House updatedHouse = houseRepository.save(existingHouse);
        return toDTO(updatedHouse);
    }

    @Override
    @Transactional
    public void deleteHouse(Long id) {
        try {
            log.info("Starting to delete house with ID: {}", id);

            House house = findHouseByIdOrThrow(id);
            log.info("Found house: {} (title: {})", id, house.getTitle());

            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            log.info("Current user: {}", currentUsername);

            User currentUser = userRepository.findByEmail(currentUsername)
                    .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUsername));

            log.info("Current user role: {}", currentUser.getRole().getName());

            // Lưu thông tin nhà trước khi xóa để tạo notification
            Long hostId = house.getHost().getId();
            String houseTitle = house.getTitle();
            boolean isAdminDeleting = currentUser.getRole().getName().equals(RoleName.ADMIN);

            // Debug logging chi tiết
            log.info("House details - ID: {}, Title: '{}', Host ID: {}", id, houseTitle, hostId);
            log.info("House title is null: {}, empty: {}", houseTitle == null, houseTitle != null && houseTitle.trim().isEmpty());

            // Đảm bảo houseTitle có giá trị
            if (houseTitle == null || houseTitle.trim().isEmpty()) {
                houseTitle = "Nhà #" + id;
                log.info("Using fallback house title: {}", houseTitle);
            }

            log.info("Final house title for notification: '{}'", houseTitle);

            if (isAdminDeleting) {
                if (house.getStatus() == House.Status.RENTED) {
                    throw new AppException(StatusCode.FORBIDDEN_ACTION, "Không thể xóa nhà đang được thuê");
                }
            }
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

            // Kiểm tra và xóa các rental records liên quan trước
            log.info("Checking for related rental records...");

            // Không cần xóa images thủ công vì đã có cascade delete
            log.info("House has {} images (will be deleted automatically by cascade)",
                house.getImages() != null ? house.getImages().size() : 0);

            log.info("Deleting house from repository");
            houseRepository.delete(house);
            log.info("House deleted successfully from repository");

            // Tạo notification cho chủ nhà nếu admin xóa nhà
            if (isAdminDeleting) {
                try {
                    log.info("Creating notification for host {} about deleted house {}", hostId, id);
                    notificationService.createHouseDeletedNotification(hostId, houseTitle, id);
                    log.info("House deleted by admin and notification sent to host {} - house: {}", hostId, houseTitle);
                } catch (Exception e) {
                    log.error("Failed to send notification for deleted house {}: {}", id, e.getMessage(), e);
                    // Không throw exception để tránh rollback transaction
                }
            }

            log.info("House deletion completed successfully for ID: {}", id);

        } catch (Exception e) {
            log.error("Error deleting house with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
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
    @Transactional(readOnly = true)
    public List<HouseDTO> getTopHousesByFavorites(int limit) {
        // Lấy tất cả nhà và sắp xếp theo số lượng yêu thích giảm dần
        return houseRepository.findAll().stream()
                .map(this::toDTO) // toDTO đã tự động set favoriteCount
                .sorted((h1, h2) -> Long.compare(h2.getFavoriteCount(), h1.getFavoriteCount())) // Sắp xếp giảm dần
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public HouseDTO updateHouseStatus(Long id, String status) {
        House house = findHouseByIdOrThrow(id);
        
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

    private void updateHouseImages(House house, List<String> newImageUrls) {
        houseImageRepository.deleteByHouse(house);
        
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

    // Dashboard statistics methods
    @Override
    @Transactional(readOnly = true)
    public long countAllHouses() {
        return houseRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countHousesByStatus(String status) {
        try {
            House.Status houseStatus = House.Status.valueOf(status.toUpperCase());
            return houseRepository.countByStatus(houseStatus);
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getRecentHousesForDashboard(int limit) {
        return houseRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(house -> {
                    Map<String, Object> houseData = new HashMap<>();
                    houseData.put("id", house.getId());
                    houseData.put("title", house.getTitle());
                    houseData.put("address", house.getAddress());
                    houseData.put("price", house.getPrice());
                    houseData.put("status", house.getStatus());
                    houseData.put("hostName", house.getHost() != null ? house.getHost().getFullName() : "N/A");
                    houseData.put("createdAt", house.getCreatedAt());
                    houseData.put("imageUrl", house.getImages() != null && !house.getImages().isEmpty() 
                        ? house.getImages().get(0).getImageUrl() : null);
                    return houseData;
                })
                .collect(Collectors.toList());
    }

}