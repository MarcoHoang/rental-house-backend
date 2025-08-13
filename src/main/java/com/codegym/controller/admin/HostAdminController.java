package com.codegym.controller.admin;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.RejectRequestPayload;
import com.codegym.dto.response.HostDTO;
import com.codegym.dto.response.HostDetailAdminDTO; // <-- Thêm import
import com.codegym.dto.response.HostRequestDTO;
import com.codegym.service.HostRequestService;
import com.codegym.service.HostService;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/admin")
@RequiredArgsConstructor
@CrossOrigin("*")
public class HostAdminController {

    private final HostService hostService;
    private final HostRequestService requestService;
    private final MessageSource messageSource;

    // --- QUẢN LÝ CÁC HOST ĐÃ ĐƯỢC DUYỆT ---
    @GetMapping("/hosts")
    public ResponseEntity<ApiResponse<Page<HostDTO>>> getAllHosts(Pageable pageable, Locale locale) {
        Page<HostDTO> hosts = hostService.getAllHosts(pageable);
        return ResponseEntity.ok(ApiResponse.success(hosts, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    // === SỬA LỖI 1: TẠO ENDPOINT XEM CHI TIẾT HOST BẰNG USER ID ===
    @GetMapping("/hosts/user/{userId}")
    public ResponseEntity<ApiResponse<HostDetailAdminDTO>> getHostDetailsByUserId(@PathVariable Long userId, Locale locale) {
        HostDetailAdminDTO hostDetails = hostService.getHostDetailsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(hostDetails, StatusCode.GET_DETAIL_SUCCESS, messageSource, locale));
    }


    // --- QUẢN LÝ CÁC YÊU CẦU LÀM HOST ---
    @GetMapping("/host-requests")
    public ResponseEntity<ApiResponse<Page<HostRequestDTO>>> getPendingRequests(Pageable pageable, Locale locale) {
        Page<HostRequestDTO> requests = requestService.findPending(pageable);
        return ResponseEntity.ok(ApiResponse.success(requests, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/host-requests/{requestId}")
    public ResponseEntity<ApiResponse<HostRequestDTO>> getRequestDetails(@PathVariable Long requestId, Locale locale) {
        HostRequestDTO requestDetails = requestService.findById(requestId);
        return ResponseEntity.ok(ApiResponse.success(requestDetails, StatusCode.GET_DETAIL_SUCCESS, messageSource, locale));
    }

    @PostMapping("/host-requests/{requestId}/approve")
    public ResponseEntity<ApiResponse<HostRequestDTO>> approveRequest(@PathVariable Long requestId, Locale locale) {
        HostRequestDTO dto = requestService.approveRequest(requestId);
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @PostMapping("/host-requests/{requestId}/reject")
    public ResponseEntity<ApiResponse<HostRequestDTO>> rejectRequest(
            @PathVariable Long requestId,
            @RequestBody @Valid RejectRequestPayload payload,
            Locale locale
    ) {
        HostRequestDTO dto = requestService.rejectRequest(requestId, payload.getReason());
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @PatchMapping("/hosts/user/{userId}/status") // <-- Dùng userId
    public ResponseEntity<ApiResponse<Void>> updateHostStatus(
            @PathVariable Long userId, // <-- Dùng userId
            @RequestBody UserAdminController.StatusUpdateRequest request,
            Locale locale
    ) {
        if (request.isActive()) {
            hostService.unlockHostByUserId(userId); // <-- Gọi phương thức mới
        } else {
            hostService.lockHostByUserId(userId); // <-- Gọi phương thức mới
        }
        return ResponseEntity.ok(ApiResponse.success(StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

}