package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.UpdateImageOrderRequest;
import com.codegym.dto.response.HouseImageDTO;
import com.codegym.entity.HouseImage;
import com.codegym.service.HouseImageService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import com.codegym.entity.House;

@RestController
@RequestMapping("/api/house-images")
@CrossOrigin("*")
@RequiredArgsConstructor
public class HouseImageController {

    private final HouseImageService houseImageService;
    private final MessageSource messageSource;

    @GetMapping("/house/{houseId}")
    public ResponseEntity<ApiResponse<List<HouseImageDTO>>> getHouseImages(
            @PathVariable Long houseId, 
            Locale locale) {
        List<HouseImage> images = houseImageService.getHouseImagesByHouseId(houseId);
        List<HouseImageDTO> imageDTOs = images.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(imageDTOs, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @PostMapping("/house/{houseId}")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<ApiResponse<HouseImageDTO>> addHouseImage(
            @PathVariable Long houseId,
            @RequestParam String imageUrl,
            Locale locale) {
        HouseImage image = houseImageService.addHouseImage(houseId, imageUrl);
        return ResponseEntity.ok(ApiResponse.success(toDTO(image), StatusCode.CREATED_SUCCESS, messageSource, locale));
    }

    @DeleteMapping("/{imageId}/house/{houseId}")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<ApiResponse<Void>> deleteHouseImage(
            @PathVariable Long imageId,
            @PathVariable Long houseId,
            Locale locale) {
        houseImageService.deleteHouseImage(imageId, houseId);
        return ResponseEntity.ok(ApiResponse.success(null, StatusCode.DELETED_SUCCESS, messageSource, locale));
    }

    @PutMapping("/house/{houseId}/order")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<ApiResponse<Void>> updateImageOrder(
            @PathVariable Long houseId,
            @RequestBody UpdateImageOrderRequest request,
            Locale locale) {
        houseImageService.updateImageOrder(houseId, request.getImageIds());
        return ResponseEntity.ok(ApiResponse.success(null, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @DeleteMapping("/house/{houseId}/all")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<ApiResponse<Void>> deleteAllHouseImages(
            @PathVariable Long houseId,
            Locale locale) {
        houseImageService.deleteAllHouseImages(houseId);
        return ResponseEntity.ok(ApiResponse.success(null, StatusCode.DELETED_SUCCESS, messageSource, locale));
    }

    // Debug endpoint - chỉ dùng trong development
    @GetMapping("/debug/house/{houseId}")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> debugHouseImages(
            @PathVariable Long houseId,
            Locale locale) {
        
        List<HouseImage> images = houseImageService.getHouseImagesByHouseId(houseId);
        
        Map<String, Object> debugInfo = new HashMap<>();
        debugInfo.put("houseId", houseId);
        debugInfo.put("totalImages", images.size());
        debugInfo.put("imageDetails", images.stream().map(img -> {
            Map<String, Object> imgInfo = new HashMap<>();
            imgInfo.put("id", img.getId());
            imgInfo.put("url", img.getImageUrl());
            imgInfo.put("sortOrder", img.getSortOrder());
            imgInfo.put("houseId", img.getHouse().getId());
            return imgInfo;
        }).collect(Collectors.toList()));
        
        return ResponseEntity.ok(ApiResponse.success(debugInfo, StatusCode.SUCCESS, messageSource, locale));
    }

    private HouseImageDTO toDTO(HouseImage image) {
        return HouseImageDTO.builder()
                .id(image.getId())
                .houseId(image.getHouse().getId())
                .imageUrl(image.getImageUrl())
                .sortOrder(image.getSortOrder())
                .createdAt(image.getCreatedAt())
                .updatedAt(image.getUpdatedAt())
                .build();
    }
}
