package com.codegym.service.impl;

import com.codegym.entity.House;
import com.codegym.entity.HouseImage;
import com.codegym.entity.User;
import com.codegym.entity.RoleName;
import com.codegym.exception.AppException;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.HouseImageRepository;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.HouseImageService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HouseImageServiceImpl implements HouseImageService {

    private final HouseImageRepository houseImageRepository;
    private final HouseRepository houseRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HouseImage> getHouseImagesByHouseId(Long houseId) {
        House house = findHouseByIdOrThrow(houseId);
        return houseImageRepository.findByHouseOrderBySortOrderAsc(house);
    }

    @Override
    @Transactional
    public HouseImage addHouseImage(Long houseId, String imageUrl) {
        House house = findHouseByIdOrThrow(houseId);
        validateHostPermission(house);
        
        // Tìm thứ tự cao nhất hiện tại
        Integer maxSortOrder = houseImageRepository.findMaxSortOrderByHouse(house);
        int newSortOrder = (maxSortOrder != null) ? maxSortOrder + 1 : 1;
        
        HouseImage houseImage = HouseImage.builder()
                .house(house)
                .imageUrl(imageUrl)
                .sortOrder(newSortOrder)
                .build();
        
        return houseImageRepository.save(houseImage);
    }

    @Override
    @Transactional
    public void deleteHouseImage(Long imageId, Long houseId) {
        House house = findHouseByIdOrThrow(houseId);
        validateHostPermission(house);
        
        HouseImage image = houseImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.IMAGE_NOT_FOUND, imageId));
        
        // Kiểm tra ảnh có thuộc về nhà này không
        if (!image.getHouse().getId().equals(houseId)) {
            throw new AppException(StatusCode.FORBIDDEN_ACTION, "Ảnh không thuộc về nhà này");
        }
        
        houseImageRepository.delete(image);
        
        // Cập nhật lại thứ tự sắp xếp
        reorderImagesAfterDelete(house);
    }

    @Override
    @Transactional
    public void updateImageOrder(Long houseId, List<Long> imageIds) {
        House house = findHouseByIdOrThrow(houseId);
        validateHostPermission(house);
        
        // Kiểm tra tất cả ảnh có thuộc về nhà này không
        List<HouseImage> existingImages = houseImageRepository.findByHouse(house);
        List<Long> existingImageIds = existingImages.stream()
                .map(HouseImage::getId)
                .collect(Collectors.toList());
        
        if (!existingImageIds.containsAll(imageIds) || imageIds.size() != existingImageIds.size()) {
            throw new AppException(StatusCode.INVALID_REQUEST, "Danh sách ảnh không hợp lệ");
        }
        
        // Cập nhật thứ tự sắp xếp
        for (int i = 0; i < imageIds.size(); i++) {
            Long imageId = imageIds.get(i);
            HouseImage image = existingImages.stream()
                    .filter(img -> img.getId().equals(imageId))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException(StatusCode.IMAGE_NOT_FOUND, imageId));
            
            image.setSortOrder(i + 1);
            houseImageRepository.save(image);
        }
    }

    @Override
    @Transactional
    public void deleteAllHouseImages(Long houseId) {
        House house = findHouseByIdOrThrow(houseId);
        validateHostPermission(house);
        houseImageRepository.deleteByHouse(house);
    }

    @Override
    @Transactional(readOnly = true)
    public House getHouseById(Long houseId) {
        return findHouseByIdOrThrow(houseId);
    }

    private House findHouseByIdOrThrow(Long id) {
        return houseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.HOUSE_NOT_FOUND, id));
    }

    private void validateHostPermission(House house) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUsername));
        
        if (!currentUser.getRole().getName().equals(RoleName.HOST) || 
            !house.getHost().getId().equals(currentUser.getId())) {
            throw new AppException(StatusCode.FORBIDDEN_ACTION, "Bạn không có quyền chỉnh sửa nhà này");
        }
    }

    private void reorderImagesAfterDelete(House house) {
        List<HouseImage> remainingImages = houseImageRepository.findByHouseOrderBySortOrderAsc(house);
        for (int i = 0; i < remainingImages.size(); i++) {
            HouseImage image = remainingImages.get(i);
            image.setSortOrder(i + 1);
            houseImageRepository.save(image);
        }
    }
}
