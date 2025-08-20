package com.codegym.controller.admin;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.HouseDTO;
import com.codegym.service.HouseService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/admin/houses")
@CrossOrigin("*")
@RequiredArgsConstructor
public class HouseAdminController {

    private final HouseService houseService;
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HouseDTO>>> getAllHouses(Locale locale) {
        List<HouseDTO> houses = houseService.getAllHouses();
        return ResponseEntity.ok(ApiResponse.success(houses, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    /**
     * Tìm kiếm nhà theo từ khóa và bộ lọc
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<HouseDTO>>> searchHouses(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String houseType,
            @RequestParam(required = false) Long hostId,
            Locale locale) {

        List<HouseDTO> houses = houseService.searchHousesForAdmin(keyword, status, houseType, hostId);
        return ResponseEntity.ok(ApiResponse.success(houses, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseDTO>> getHouseById(@PathVariable Long id, Locale locale) {
        HouseDTO house = houseService.getHouseById(id);
        return ResponseEntity.ok(ApiResponse.success(house, StatusCode.SUCCESS, messageSource, locale));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHouse(@PathVariable Long id, Locale locale) {
        houseService.deleteHouse(id);
        return ResponseEntity.ok(ApiResponse.success(StatusCode.DELETED_SUCCESS, messageSource, locale));
    }
} 