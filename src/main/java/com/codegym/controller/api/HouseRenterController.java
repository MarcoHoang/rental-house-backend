package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.HouseDTO;
import com.codegym.dto.response.HouseRenterDTO;
import com.codegym.service.HouseRenterService;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/landlords")
@CrossOrigin("*")
public class HouseRenterController {

    @Autowired
    private HouseRenterService houseRenterService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HouseRenterDTO>>> getAllHouseRenters() {
        List<HouseRenterDTO> landlords = houseRenterService.getAllHouseRenters();
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, landlords));
    }

    @PutMapping("/{id}/lock")
    public ResponseEntity<ApiResponse<Void>> lockHouseRenter(@PathVariable Long id) {
        houseRenterService.lockHouseRenter(id);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS));
    }

    @PutMapping("/{id}/unlock")
    public ResponseEntity<ApiResponse<Void>> unlockHouseRenter(@PathVariable Long id) {
        houseRenterService.unlockHouseRenter(id);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS));
    }

    @GetMapping("/{id}/houses")
    public ResponseEntity<ApiResponse<List<HouseDTO>>> getHouseRenterHouses(@PathVariable Long id) {
        List<HouseDTO> houses = houseRenterService.getHouseRenterHouses(id);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, houses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseRenterDTO>> getHouseRenterById(@PathVariable Long id) {
        HouseRenterDTO dto = houseRenterService.getHouseRenterById(id);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, dto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HouseRenterDTO>> createHouseRenter(@RequestBody @Valid HouseRenterDTO dto) {
        HouseRenterDTO created = houseRenterService.createHouseRenter(dto);
        return new ResponseEntity<>(new ApiResponse<>(StatusCode.SUCCESS, created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HouseRenterDTO>> updateHouseRenter(@PathVariable Long id, @RequestBody @Valid HouseRenterDTO dto) {
        HouseRenterDTO updated = houseRenterService.updateHouseRenter(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHouseRenter(@PathVariable Long id) {
        houseRenterService.deleteHouseRenter(id);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS));
    }
}