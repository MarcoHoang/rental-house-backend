package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.RejectRequestPayload;
import com.codegym.dto.response.HouseRenterRequestDTO;
import com.codegym.service.HouseRenterRequestService;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/house-renter-requests")
@RequiredArgsConstructor
@CrossOrigin("*")
public class HouseRenterRequestController {

    private final HouseRenterRequestService houseRenterRequestService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HouseRenterRequestDTO>>> getAllRequests() {
        List<HouseRenterRequestDTO> requests = houseRenterRequestService.findAll();
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, requests));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HouseRenterRequestDTO>> createRequest(@RequestBody @Valid HouseRenterRequestDTO dto) {
        HouseRenterRequestDTO created = houseRenterRequestService.createRequest(dto);
        return new ResponseEntity<>(new ApiResponse<>(StatusCode.SUCCESS, created), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<HouseRenterRequestDTO>> getByUserId(@PathVariable Long userId) {
        HouseRenterRequestDTO dto = houseRenterRequestService.findByUserId(userId);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, dto));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<HouseRenterRequestDTO>> approve(@PathVariable Long id) {
        HouseRenterRequestDTO dto = houseRenterRequestService.approveRequest(id);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, dto));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<HouseRenterRequestDTO>> reject(@PathVariable Long id, @RequestBody @Valid RejectRequestPayload payload) {
        HouseRenterRequestDTO dto = houseRenterRequestService.rejectRequest(id, payload.getReason());
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, dto));
    }
}