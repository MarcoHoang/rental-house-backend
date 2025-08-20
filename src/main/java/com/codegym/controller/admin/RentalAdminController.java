package com.codegym.controller.admin;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.RentalDTO;
import com.codegym.service.RentalService;
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
@RequestMapping("${api.prefix}/admin/rentals")
@RequiredArgsConstructor
@CrossOrigin("*")
@PreAuthorize("hasRole('ADMIN')")
public class RentalAdminController {

    private final RentalService rentalService;
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<RentalDTO>>> getAllRentals(Pageable pageable, Locale locale) {
        // Sắp xếp theo thời gian tạo mới nhất (mới nhất ở trên)
        Pageable sortedPageable = PageRequest.of(
            pageable.getPageNumber(), 
            pageable.getPageSize(), 
            Sort.by(Sort.Direction.DESC, "createdAt")
        );
        Page<RentalDTO> rentals = rentalService.getAllRentalsForAdmin(sortedPageable);
        return ResponseEntity.ok(ApiResponse.success(rentals, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<RentalDTO>>> searchRentals(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String houseType,
            Pageable pageable,
            Locale locale) {
        Page<RentalDTO> rentals = rentalService.searchRentalsForAdmin(keyword, status, houseType, pageable);
        return ResponseEntity.ok(ApiResponse.success(rentals, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RentalDTO>> getRentalById(@PathVariable Long id, Locale locale) {
        RentalDTO rental = rentalService.getRentalByIdForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(rental, StatusCode.GET_DETAIL_SUCCESS, messageSource, locale));
    }
}
