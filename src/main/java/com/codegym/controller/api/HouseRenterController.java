package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.HouseDTO;
import com.codegym.dto.response.HouseRenterDTO;
import com.codegym.service.HouseRenterService;
import com.codegym.utils.MessageUtil;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/landlords")
@CrossOrigin("*")
@RequiredArgsConstructor
public class HouseRenterController {

    private final HouseRenterService houseRenterService;
    private final MessageUtil messageUtil;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HouseRenterDTO>>> getAllHouseRenters() {
        List<HouseRenterDTO> landlords = houseRenterService.getAllHouseRenters();
        String message = messageUtil.getMessage("houserenter.list.found");
        ApiResponse<List<HouseRenterDTO>> response = new ApiResponse<>(StatusCode.GET_LIST_SUCCESS.getCode(), message, landlords);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/lock")
    public ResponseEntity<ApiResponse<Void>> lockHouseRenter(@PathVariable Long id) {
        houseRenterService.lockHouseRenter(id);
        String message = messageUtil.getMessage("houserenter.locked");
        ApiResponse<Void> response = new ApiResponse<>(StatusCode.UPDATED_SUCCESS.getCode(), message);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/unlock")
    public ResponseEntity<ApiResponse<Void>> unlockHouseRenter(@PathVariable Long id) {
        houseRenterService.unlockHouseRenter(id);
        String message = messageUtil.getMessage("houserenter.unlocked");
        ApiResponse<Void> response = new ApiResponse<>(StatusCode.UPDATED_SUCCESS.getCode(), message);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/houses")
    public ResponseEntity<ApiResponse<List<HouseDTO>>> getHouseRenterHouses(@PathVariable Long id) {
        List<HouseDTO> houses = houseRenterService.getHouseRenterHouses(id);
        String message = messageUtil.getMessage("houserenter.houses.found");
        ApiResponse<List<HouseDTO>> response = new ApiResponse<>(StatusCode.GET_LIST_SUCCESS.getCode(), message, houses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseRenterDTO>> getHouseRenterById(@PathVariable Long id) {
        HouseRenterDTO dto = houseRenterService.getHouseRenterById(id);
        String message = messageUtil.getMessage("houserenter.get.found");
        ApiResponse<HouseRenterDTO> response = new ApiResponse<>(StatusCode.SUCCESS.getCode(), message, dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HouseRenterDTO>> createHouseRenter(@RequestBody @Valid HouseRenterDTO dto) {
        HouseRenterDTO created = houseRenterService.createHouseRenter(dto);
        String message = messageUtil.getMessage("houserenter.created");
        ApiResponse<HouseRenterDTO> response = new ApiResponse<>(StatusCode.CREATED_SUCCESS.getCode(), message, created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseRenterDTO>> updateHouseRenter(@PathVariable Long id, @RequestBody @Valid HouseRenterDTO dto) {
        HouseRenterDTO updated = houseRenterService.updateHouseRenter(id, dto);
        String message = messageUtil.getMessage("houserenter.updated");
        ApiResponse<HouseRenterDTO> response = new ApiResponse<>(StatusCode.UPDATED_SUCCESS.getCode(), message, updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHouseRenter(@PathVariable Long id) {
        houseRenterService.deleteHouseRenter(id);
        String message = messageUtil.getMessage("houserenter.deleted");
        ApiResponse<Void> response = new ApiResponse<>(StatusCode.DELETED_SUCCESS.getCode(), message);
        return ResponseEntity.ok(response);
    }
}