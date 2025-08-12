package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.RejectRequestPayload;
import com.codegym.dto.response.HouseRenterRequestDTO;
import com.codegym.service.HouseRenterRequestService;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/house-renter-requests")
@RequiredArgsConstructor
@CrossOrigin("*")
public class HouseRenterRequestController {

    private final HouseRenterRequestService houseRenterRequestService;
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HouseRenterRequestDTO>>> getAllRequests(Locale locale) {
        List<HouseRenterRequestDTO> requests = houseRenterRequestService.findAll();
        return ResponseEntity.ok(ApiResponse.success(requests, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HouseRenterRequestDTO>> createRequest(@RequestBody @Valid HouseRenterRequestDTO dto, Locale locale) {
        HouseRenterRequestDTO created = houseRenterRequestService.createRequest(dto);
        return new ResponseEntity<>(ApiResponse.success(created, StatusCode.CREATED_SUCCESS, messageSource, locale), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<HouseRenterRequestDTO>> getByUserId(@PathVariable Long userId, Locale locale) {
        HouseRenterRequestDTO dto = houseRenterRequestService.findByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.SUCCESS, messageSource, locale));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<HouseRenterRequestDTO>> approve(@PathVariable Long id, Locale locale) {
        HouseRenterRequestDTO dto = houseRenterRequestService.approveRequest(id);
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<HouseRenterRequestDTO>> reject(@PathVariable Long id, @RequestBody @Valid RejectRequestPayload payload, Locale locale) {
        HouseRenterRequestDTO dto = houseRenterRequestService.rejectRequest(id, payload.getReason());
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }
}