package com.codegym.controller.api;

import com.codegym.dto.response.LandlordRequestDTO;
import com.codegym.service.LandlordRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/landlord-requests")
@RequiredArgsConstructor
public class LandlordRequestController {

    private final LandlordRequestService landlordRequestService;

    @GetMapping
    public ResponseEntity<List<LandlordRequestDTO>> getAllRequests() {
        return ResponseEntity.ok(landlordRequestService.findAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<LandlordRequestDTO> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(landlordRequestService.findByUserId(userId));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<LandlordRequestDTO> approve(@PathVariable Long id) {
        return ResponseEntity.ok(landlordRequestService.approveRequest(id));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<LandlordRequestDTO> reject(@PathVariable Long id, @RequestParam String reason) {
        return ResponseEntity.ok(landlordRequestService.rejectRequest(id, reason));
    }
}
