package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.HouseDTO;
import com.codegym.service.HouseService;
import com.codegym.utils.MessageUtil;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/houses")
@CrossOrigin("*")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;
    private final MessageUtil messageUtil;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HouseDTO>>> getAllHouses() {
        List<HouseDTO> houses = houseService.getAllHouses();
        String message = messageUtil.getMessage("house.list.found");
        ApiResponse<List<HouseDTO>> response = new ApiResponse<>(StatusCode.GET_LIST_SUCCESS.getCode(), message, houses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<HouseDTO>>> searchHouses(@RequestParam(required = false) String keyword) {
        List<HouseDTO> houses = houseService.searchHouses(keyword);
        String message = messageUtil.getMessage("house.search.found");
        ApiResponse<List<HouseDTO>> response = new ApiResponse<>(StatusCode.GET_LIST_SUCCESS.getCode(), message, houses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top")
    public ResponseEntity<ApiResponse<List<HouseDTO>>> getTopHouses() {
        List<HouseDTO> houses = houseService.getTopHouses();
        String message = messageUtil.getMessage("house.top.found");
        ApiResponse<List<HouseDTO>> response = new ApiResponse<>(StatusCode.GET_LIST_SUCCESS.getCode(), message, houses);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<HouseDTO>> updateHouseStatus(@PathVariable Long id, @RequestParam String status) {
        HouseDTO updated = houseService.updateHouseStatus(id, status);
        String message = messageUtil.getMessage("house.status.updated");
        ApiResponse<HouseDTO> response = new ApiResponse<>(StatusCode.UPDATED_SUCCESS.getCode(), message, updated);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/map")
    public ResponseEntity<ApiResponse<HouseDTO>> getHouseMap(@PathVariable Long id) {
        HouseDTO dto = houseService.getHouseById(id);
        String message = messageUtil.getMessage("house.map.found");
        ApiResponse<HouseDTO> response = new ApiResponse<>(StatusCode.SUCCESS.getCode(), message, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<ApiResponse<List<String>>> getHouseImages(@PathVariable Long id) {
        List<String> images = houseService.getHouseImages(id);
        String message = messageUtil.getMessage("house.images.found");
        ApiResponse<List<String>> response = new ApiResponse<>(StatusCode.SUCCESS.getCode(), message, images);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseDTO>> getHouseById(@PathVariable Long id) {
        HouseDTO dto = houseService.getHouseById(id);
        String message = messageUtil.getMessage("house.get.found");
        ApiResponse<HouseDTO> response = new ApiResponse<>(StatusCode.SUCCESS.getCode(), message, dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HouseDTO>> createHouse(@RequestBody @Valid HouseDTO dto) {
        HouseDTO created = houseService.createHouse(dto);
        String message = messageUtil.getMessage("house.created");
        ApiResponse<HouseDTO> response = new ApiResponse<>(StatusCode.CREATED_SUCCESS.getCode(), message, created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseDTO>> updateHouse(@PathVariable Long id, @RequestBody @Valid HouseDTO dto) {
        HouseDTO updated = houseService.updateHouse(id, dto);
        String message = messageUtil.getMessage("house.updated");
        ApiResponse<HouseDTO> response = new ApiResponse<>(StatusCode.UPDATED_SUCCESS.getCode(), message, updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHouse(@PathVariable Long id) {
        houseService.deleteHouse(id);
        String message = messageUtil.getMessage("house.deleted");
        ApiResponse<Void> response = new ApiResponse<>(StatusCode.DELETED_SUCCESS.getCode(), message);
        return ResponseEntity.ok(response);
    }
}