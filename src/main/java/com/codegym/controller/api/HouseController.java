package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.HouseRequest;
import com.codegym.dto.response.HouseDTO;
import com.codegym.dto.response.GeocodingResponse;
import com.codegym.service.HouseService;
import com.codegym.service.GeocodingService;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/houses")
@CrossOrigin("*")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;
    private final GeocodingService geocodingService;
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HouseDTO>>> getAllHouses(Locale locale) {
        List<HouseDTO> houses = houseService.getAllHouses();
        return ResponseEntity.ok(ApiResponse.success(houses, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<HouseDTO>>> searchHouses(@RequestParam(required = false) String keyword, Locale locale) {
        List<HouseDTO> houses = houseService.searchHouses(keyword);
        return ResponseEntity.ok(ApiResponse.success(houses, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/top")
    public ResponseEntity<ApiResponse<List<HouseDTO>>> getTopHouses(Locale locale) {
        List<HouseDTO> houses = houseService.getTopHouses();
        return ResponseEntity.ok(ApiResponse.success(houses, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/top-favorites")
    public ResponseEntity<ApiResponse<List<HouseDTO>>> getTopHousesByFavorites(
            @RequestParam(defaultValue = "5") int limit, 
            Locale locale) {
        List<HouseDTO> houses = houseService.getTopHousesByFavorites(limit);
        return ResponseEntity.ok(ApiResponse.success(houses, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<HouseDTO>> updateHouseStatus(@PathVariable Long id, @RequestParam String status, Locale locale) {
        HouseDTO updated = houseService.updateHouseStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(updated, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @GetMapping("/{id}/map")
    public ResponseEntity<ApiResponse<HouseDTO>> getHouseMap(@PathVariable Long id, Locale locale) {
        HouseDTO dto = houseService.getHouseById(id);
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.SUCCESS, messageSource, locale));
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<ApiResponse<List<String>>> getHouseImages(@PathVariable Long id, Locale locale) {
        List<String> images = houseService.getHouseImages(id);
        return ResponseEntity.ok(ApiResponse.success(images, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseDTO>> getHouseById(@PathVariable Long id, Locale locale) {
        HouseDTO dto = houseService.getHouseById(id);
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.SUCCESS, messageSource, locale));
    }

    @GetMapping("/my-houses")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<ApiResponse<List<HouseDTO>>> getMyHouses(Locale locale) {
        List<HouseDTO> houses = houseService.getHousesByCurrentHost();
        return ResponseEntity.ok(ApiResponse.success(houses, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @PostMapping
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<ApiResponse<HouseDTO>> createHouse(@RequestBody @Valid HouseRequest request, Locale locale) {
        HouseDTO created = houseService.createHouse(request);
        return new ResponseEntity<>(ApiResponse.success(created, StatusCode.CREATED_SUCCESS, messageSource, locale), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<ApiResponse<HouseDTO>> updateHouse(@PathVariable Long id, @RequestBody @Valid HouseRequest request, Locale locale) {
        HouseDTO updated = houseService.updateHouse(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<ApiResponse<Void>> deleteHouse(@PathVariable Long id, Locale locale) {
        houseService.deleteHouse(id);
        return ResponseEntity.ok(ApiResponse.success(StatusCode.DELETED_SUCCESS, messageSource, locale));
    }

    /**
     * Kiểm tra xem địa chỉ có thể geocode được không
     */
    @GetMapping("/validate-address")
    public ResponseEntity<ApiResponse<GeocodingResponse>> validateAddress(@RequestParam String address, Locale locale) {
        boolean isValid = geocodingService.isAddressValid(address);
        String formattedAddress = null;
        
        if (isValid) {
            formattedAddress = geocodingService.getFormattedAddress(address);
        }
        
        GeocodingResponse response = GeocodingResponse.builder()
                .isValid(isValid)
                .formattedAddress(formattedAddress)
                .originalAddress(address)
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response, StatusCode.SUCCESS, messageSource, locale));
    }

    /**
     * Lấy tọa độ từ địa chỉ
     */
    @GetMapping("/geocode")
    public ResponseEntity<ApiResponse<GeocodingResponse>> geocodeAddress(@RequestParam String address, Locale locale) {
        GeocodingResponse response = geocodingService.getGeocodingResponse(address);
        
        if (response.isValid()) {
            return ResponseEntity.ok(ApiResponse.success(response, StatusCode.SUCCESS, messageSource, locale));
        } else {
            return ResponseEntity.ok(ApiResponse.success(response, StatusCode.GEOCODING_FAILED, messageSource, locale));
        }
    }

    /**
     * Xóa cache geocoding
     */
    @DeleteMapping("/geocode/cache")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> clearGeocodingCache(Locale locale) {
        geocodingService.clearCache();
        return ResponseEntity.ok(ApiResponse.success("Cache đã được xóa", StatusCode.SUCCESS, messageSource, locale));
    }

    /**
     * Lấy thống kê cache geocoding
     */
    @GetMapping("/geocode/cache/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> getGeocodingCacheStats(Locale locale) {
        String stats = geocodingService.getCacheStats();
        return ResponseEntity.ok(ApiResponse.success(stats, StatusCode.SUCCESS, messageSource, locale));
    }

    /**
     * Test endpoint để kiểm tra thông tin user hiện tại
     */
    @GetMapping("/test-current-user")
    public ResponseEntity<ApiResponse<Map<String, Object>>> testCurrentUser(Locale locale) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("email", currentUsername);
        userInfo.put("authenticated", SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
        userInfo.put("authorities", SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(Object::toString)
                .collect(Collectors.toList()));
        
        return ResponseEntity.ok(ApiResponse.success(userInfo, StatusCode.SUCCESS, messageSource, locale));
    }
}