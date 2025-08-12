package com.codegym.controller.admin;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.RejectRequestPayload;
import com.codegym.dto.response.HostDTO;
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
@RequestMapping("${api.prefix}/admin") // Tất cả API bắt đầu bằng /api/admin
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

    // --- QUẢN LÝ CÁC YÊU CẦU LÀM HOST ---
    @GetMapping("/host-requests")
    public ResponseEntity<ApiResponse<Page<HostRequestDTO>>> getPendingRequests(Pageable pageable, Locale locale) {
        Page<HostRequestDTO> requests = requestService.findPending(pageable);
        return ResponseEntity.ok(ApiResponse.success(requests, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @PostMapping("/host-requests/{id}/approve")
    public ResponseEntity<ApiResponse<HostRequestDTO>> approveRequest(@PathVariable Long id, Locale locale) {
        HostRequestDTO dto = requestService.approveRequest(id);
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @PostMapping("/host-requests/{id}/reject")
    public ResponseEntity<ApiResponse<HostRequestDTO>> rejectRequest(@PathVariable Long id, @RequestBody @Valid RejectRequestPayload payload, Locale locale) {
        HostRequestDTO dto = requestService.rejectRequest(id, payload.getReason());
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @PatchMapping("/hosts/{hostId}/status")
    public ResponseEntity<ApiResponse<Void>> updateHostStatus(
            @PathVariable Long hostId,
            @RequestBody UserAdminController.StatusUpdateRequest request, // Tái sử dụng DTO từ UserAdminController
            Locale locale
    ) {
        if (request.isActive()) {
            hostService.unlockHost(hostId);
        } else {
            hostService.lockHost(hostId);
        }
        return ResponseEntity.ok(ApiResponse.success(StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }
}