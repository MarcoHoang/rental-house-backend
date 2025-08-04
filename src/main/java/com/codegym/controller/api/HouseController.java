package com.codegym.controller.api;

import com.codegym.dto.response.HouseDTO;
import com.codegym.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/houses")
@CrossOrigin("*")
public class HouseController {

    @Autowired
    private HouseService houseService;

    @GetMapping
    public ResponseEntity<List<HouseDTO>> getAllHouses() {
        return ResponseEntity.ok(houseService.getAllHouses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HouseDTO> getHouseById(@PathVariable Long id) {
        HouseDTO dto = houseService.getHouseById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<HouseDTO> createHouse(@RequestBody HouseDTO dto) {
        return ResponseEntity.ok(houseService.createHouse(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HouseDTO> updateHouse(@PathVariable Long id, @RequestBody HouseDTO dto) {
        return ResponseEntity.ok(houseService.updateHouse(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHouse(@PathVariable Long id) {
        houseService.deleteHouse(id);
        return ResponseEntity.noContent().build();
    }
}
