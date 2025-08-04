package com.codegym.controller.api;

import com.codegym.dto.response.LandlordDTO;
import com.codegym.service.LandlordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/landlords")
@CrossOrigin("*")
public class LandlordController {

    @Autowired
    private LandlordService landlordService;

    @GetMapping
    public ResponseEntity<List<LandlordDTO>> getAllLandlords() {
        return ResponseEntity.ok(landlordService.getAllLandlords());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LandlordDTO> getLandlordById(@PathVariable Long id) {
        LandlordDTO dto = landlordService.getLandlordById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<LandlordDTO> createLandlord(@RequestBody LandlordDTO dto) {
        return ResponseEntity.ok(landlordService.createLandlord(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LandlordDTO> updateLandlord(@PathVariable Long id, @RequestBody LandlordDTO dto) {
        return ResponseEntity.ok(landlordService.updateLandlord(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLandlord(@PathVariable Long id) {
        landlordService.deleteLandlord(id);
        return ResponseEntity.noContent().build();
    }
}
