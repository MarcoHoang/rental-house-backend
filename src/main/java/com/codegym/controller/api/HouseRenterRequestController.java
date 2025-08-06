package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.RejectRequestPayload;
import com.codegym.dto.response.HouseRenterRequestDTO;
import com.codegym.service.HouseRenterRequestService;
import com.codegym.utils.MessageUtil;
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
    private final MessageUtil messageUtil;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HouseRenterRequestDTO>>> getAllRequests() {
        List<HouseRenterRequestDTO> requests = houseRenterRequestService.findAll();
        String message = messageUtil.getMessage("request.list.found");
        ApiResponse<List<HouseRenterRequestDTO>> response = new ApiResponse<>(StatusCode.GET_LIST_SUCCESS.getCode(), message, requests);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HouseRenterRequestDTO>> createRequest(@RequestBody @Valid HouseRenterRequestDTO dto) {
        HouseRenterRequestDTO created = houseRenterRequestService.createRequest(dto);
        String message = messageUtil.getMessage("request.created");
        ApiResponse<HouseRenterRequestDTO> response = new ApiResponse<>(StatusCode.CREATED_SUCCESS.getCode(), message, created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<HouseRenterRequestDTO>> getByUserId(@PathVariable Long userId) {
        HouseRenterRequestDTO dto = houseRenterRequestService.findByUserId(userId);
        String message = messageUtil.getMessage("request.get.byUser.found");
        ApiResponse<HouseRenterRequestDTO> response = new ApiResponse<>(StatusCode.SUCCESS.getCode(), message, dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<HouseRenterRequestDTO>> approve(@PathVariable Long id) {
        HouseRenterRequestDTO dto = houseRenterRequestService.approveRequest(id);
        String message = messageUtil.getMessage("request.approved");
        ApiResponse<HouseRenterRequestDTO> response = new ApiResponse<>(StatusCode.UPDATED_SUCCESS.getCode(), message, dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<HouseRenterRequestDTO>> reject(@PathVariable Long id, @RequestBody @Valid RejectRequestPayload payload) {
        HouseRenterRequestDTO dto = houseRenterRequestService.rejectRequest(id, payload.getReason());
        String message = messageUtil.getMessage("request.rejected");
        ApiResponse<HouseRenterRequestDTO> response = new ApiResponse<>(StatusCode.UPDATED_SUCCESS.getCode(), message, dto);
        return ResponseEntity.ok(response);
    }
}