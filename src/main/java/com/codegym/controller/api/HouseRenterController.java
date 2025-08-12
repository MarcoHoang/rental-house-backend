package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.HouseDTO;
import com.codegym.dto.response.HouseRenterDTO;
import com.codegym.service.HouseRenterService;
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
@RequestMapping("/api/houseRenters")
@CrossOrigin("*")
@RequiredArgsConstructor
public class HouseRenterController {

    private final HouseRenterService houseRenterService;
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HouseRenterDTO>>> getAllHouseRenters(Locale locale) {
        List<HouseRenterDTO> landlords = houseRenterService.getAllHouseRenters();
        return ResponseEntity.ok(ApiResponse.success(landlords, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @PutMapping("/{id}/lock")
    public ResponseEntity<ApiResponse<Void>> lockHouseRenter(@PathVariable Long id, Locale locale) {
        houseRenterService.lockHouseRenter(id);
        return ResponseEntity.ok(ApiResponse.success(StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @PutMapping("/{id}/unlock")
    public ResponseEntity<ApiResponse<Void>> unlockHouseRenter(@PathVariable Long id, Locale locale) {
        houseRenterService.unlockHouseRenter(id);
        return ResponseEntity.ok(ApiResponse.success(StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @GetMapping("/{id}/houses")
    public ResponseEntity<ApiResponse<List<HouseDTO>>> getHouseRenterHouses(@PathVariable Long id, Locale locale) {
        List<HouseDTO> houses = houseRenterService.getHouseRenterHouses(id);
        return ResponseEntity.ok(ApiResponse.success(houses, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseRenterDTO>> getHouseRenterById(@PathVariable Long id, Locale locale) {
        HouseRenterDTO dto = houseRenterService.getHouseRenterById(id);
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.SUCCESS, messageSource, locale));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HouseRenterDTO>> createHouseRenter(@RequestBody @Valid HouseRenterDTO dto, Locale locale) {
        HouseRenterDTO created = houseRenterService.createHouseRenter(dto);
        return new ResponseEntity<>(ApiResponse.success(created, StatusCode.CREATED_SUCCESS, messageSource, locale), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseRenterDTO>> updateHouseRenter(@PathVariable Long id, @RequestBody @Valid HouseRenterDTO dto, Locale locale) {
        HouseRenterDTO updated = houseRenterService.updateHouseRenter(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHouseRenter(@PathVariable Long id, Locale locale) {
        houseRenterService.deleteHouseRenter(id);
        return ResponseEntity.ok(ApiResponse.success(StatusCode.DELETED_SUCCESS, messageSource, locale));
    }
}