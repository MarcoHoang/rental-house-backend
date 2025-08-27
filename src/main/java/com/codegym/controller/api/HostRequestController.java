package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.RejectRequestPayload;
import com.codegym.dto.response.HostRequestDTO;
import com.codegym.service.HostRequestService;
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
@RequestMapping("/api/host-requests")
@RequiredArgsConstructor
public class HostRequestController {

    private final HostRequestService hostRequestService;
    private final MessageSource messageSource;



    @PostMapping
    public ResponseEntity<ApiResponse<HostRequestDTO>> createRequest(@RequestBody @Valid HostRequestDTO dto, Locale locale) {
        HostRequestDTO created = hostRequestService.createRequest(dto);
        return new ResponseEntity<>(ApiResponse.success(created, StatusCode.CREATED_SUCCESS, messageSource, locale), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<HostRequestDTO>> getByUserId(@PathVariable Long userId, Locale locale) {
        HostRequestDTO dto = hostRequestService.findByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.SUCCESS, messageSource, locale));
    }

    @GetMapping("/my-request")
    public ResponseEntity<ApiResponse<HostRequestDTO>> getMyRequest(Locale locale) {
        HostRequestDTO dto = hostRequestService.getCurrentUserRequest();
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.SUCCESS, messageSource, locale));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<HostRequestDTO>> approve(@PathVariable Long id, Locale locale) {
        HostRequestDTO dto = hostRequestService.approveRequest(id);
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<HostRequestDTO>> reject(@PathVariable Long id, @RequestBody @Valid RejectRequestPayload payload, Locale locale) {
        HostRequestDTO dto = hostRequestService.rejectRequest(id, payload.getReason());
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }
}

