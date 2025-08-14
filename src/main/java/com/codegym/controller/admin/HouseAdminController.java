package com.codegym.controller.admin;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.HouseDTO;
import com.codegym.service.HouseService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/admin/houses")
@CrossOrigin("*")
@RequiredArgsConstructor
public class HouseAdminController {

    private final HouseService houseService;
    private final MessageSource messageSource;

    /**
     * Lấy danh sách tất cả nhà với phân trang
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<HouseDTO>>> getAllHouses(
            Pageable pageable,
            Locale locale) {

        Page<HouseDTO> houses = houseService.getAllHousesForAdmin(pageable);
        return ResponseEntity.ok(ApiResponse.success(houses, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    /**
     * Lấy chi tiết một nhà theo ID
     */
    @GetMapping("/{houseId}")
    public ResponseEntity<ApiResponse<HouseDTO>> getHouseById(
            @PathVariable Long houseId,
            Locale locale) {

        HouseDTO house = houseService.getHouseById(houseId);
        return ResponseEntity.ok(ApiResponse.success(house, StatusCode.SUCCESS, messageSource, locale));
    }

    /**
     * Cập nhật trạng thái nhà
     */
    @PatchMapping("/{houseId}/status")
    public ResponseEntity<ApiResponse<HouseDTO>> updateHouseStatus(
            @PathVariable Long houseId,
            @RequestBody StatusUpdateRequest request,
            Locale locale) {

        HouseDTO updatedHouse = houseService.updateHouseStatus(houseId, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success(updatedHouse, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    /**
     * Xóa nhà
     */
    @DeleteMapping("/{houseId}")
    public ResponseEntity<ApiResponse<Void>> deleteHouse(
            @PathVariable Long houseId,
            Locale locale) {

        houseService.deleteHouse(houseId);
        return ResponseEntity.ok(ApiResponse.success(StatusCode.DELETED_SUCCESS, messageSource, locale));
    }

    /**
     * DTO cho request cập nhật trạng thái
     */
    public static class StatusUpdateRequest {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
} 