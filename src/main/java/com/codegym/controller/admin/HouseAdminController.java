package com.codegym.controller.admin;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.HouseDTO;
import com.codegym.service.HouseService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<HouseDTO>>> getAllHouses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort,
            Locale locale) {

        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1] : "asc";

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort.Order order = new Sort.Order(direction, sortField);

        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        Page<HouseDTO> housePage = houseService.getAllHousesWithPagination(pageable);
        
        return ResponseEntity.ok(ApiResponse.success(housePage, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    /**
     * Tìm kiếm nhà theo từ khóa và bộ lọc
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<HouseDTO>> getHouseById(@PathVariable Long id, Locale locale) {
        HouseDTO house = houseService.getHouseById(id);
        return ResponseEntity.ok(ApiResponse.success(house, StatusCode.SUCCESS, messageSource, locale));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteHouse(@PathVariable Long id, Locale locale) {
        houseService.deleteHouse(id);
        return ResponseEntity.ok(ApiResponse.success(StatusCode.DELETED_SUCCESS, messageSource, locale));
    }
} 