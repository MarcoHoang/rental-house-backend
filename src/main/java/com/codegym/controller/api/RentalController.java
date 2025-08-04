package com.codegym.controller.api;

import com.codegym.dto.response.RentalDTO;
import com.codegym.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@CrossOrigin("*")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @GetMapping
    public ResponseEntity<List<RentalDTO>> getAll() {
        return ResponseEntity.ok(rentalService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RentalDTO> create(@RequestBody RentalDTO rentalDTO) {
        return ResponseEntity.ok(rentalService.create(rentalDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RentalDTO> update(@PathVariable Long id, @RequestBody RentalDTO rentalDTO) {
        return ResponseEntity.ok(rentalService.update(id, rentalDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rentalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
