package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.HouseRequest;
import com.codegym.dto.response.HouseDTO;
import com.codegym.service.HouseService;
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
@RequestMapping("/api/houses")
@CrossOrigin("*")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HouseDTO>>> getAllHouses(Locale locale) {
        List<HouseDTO> houses = houseService.getAllHouses();
        return ResponseEntity.ok(ApiResponse.success(houses, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<HouseDTO>>> searchHouses(@RequestParam(required = false) String keyword, Locale locale) {
        List<HouseDTO> houses = houseService.searchHouses(keyword);
        return ResponseEntity.ok(ApiResponse.success(houses, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/top")
    public ResponseEntity<ApiResponse<List<HouseDTO>>> getTopHouses(Locale locale) {
        List<HouseDTO> houses = houseService.getTopHouses();
        return ResponseEntity.ok(ApiResponse.success(houses, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<HouseDTO>> updateHouseStatus(@PathVariable Long id, @RequestParam String status, Locale locale) {
        HouseDTO updated = houseService.updateHouseStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(updated, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @GetMapping("/{id}/map")
    public ResponseEntity<ApiResponse<HouseDTO>> getHouseMap(@PathVariable Long id, Locale locale) {
        HouseDTO dto = houseService.getHouseById(id);
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.SUCCESS, messageSource, locale));
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<ApiResponse<List<String>>> getHouseImages(@PathVariable Long id, Locale locale) {
        List<String> images = houseService.getHouseImages(id);
        return ResponseEntity.ok(ApiResponse.success(images, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseDTO>> getHouseById(@PathVariable Long id, Locale locale) {
        HouseDTO dto = houseService.getHouseById(id);
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.SUCCESS, messageSource, locale));
    }

    @GetMapping("/my-houses")
    public ResponseEntity<ApiResponse<List<HouseDTO>>> getMyHouses(Locale locale) {
        List<HouseDTO> houses = houseService.getHousesByCurrentHost();
        return ResponseEntity.ok(ApiResponse.success(houses, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HouseDTO>> createHouse(@RequestBody @Valid HouseRequest request, Locale locale) {
        HouseDTO created = houseService.createHouse(request);
        return new ResponseEntity<>(ApiResponse.success(created, StatusCode.CREATED_SUCCESS, messageSource, locale), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseDTO>> updateHouse(@PathVariable Long id, @RequestBody @Valid HouseRequest request, Locale locale) {
        HouseDTO updated = houseService.updateHouse(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHouse(@PathVariable Long id, Locale locale) {
        houseService.deleteHouse(id);
        return ResponseEntity.ok(ApiResponse.success(StatusCode.DELETED_SUCCESS, messageSource, locale));
    }
}