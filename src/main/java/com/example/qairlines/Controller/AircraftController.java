package com.example.qairlines.Controller;

import com.example.qairlines.DTO.AirCraftDTO;
import com.example.qairlines.Model.AirCraft;
import com.example.qairlines.Services.AircraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aircrafts")
@RequiredArgsConstructor
public class AircraftController {
    private final AircraftService aircraftService;

    // Get all aircrafts
    @GetMapping("/all")
    public ResponseEntity<List<AirCraftDTO>> getAllAirCrafts() {
        List<AirCraftDTO> aircrafts = aircraftService.getAllAirCrafts();
        if (aircrafts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(aircrafts);
        }
        return ResponseEntity.ok(aircrafts);
    }

    // Add a new aircraft
    @PostMapping("/add")
    public ResponseEntity<AirCraft> addAirCraft(@RequestBody AirCraft airCraft) {
        try {
            AirCraft newAirCraft = aircraftService.addAirCraft(airCraft);
            return ResponseEntity.status(HttpStatus.CREATED).body(newAirCraft);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Update an aircraft by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<AirCraft> updateAirCraft(@PathVariable Long id, @RequestBody AirCraft airCraft) {
        try {
            AirCraft updatedAirCraft = aircraftService.updateAirCraft(id, airCraft);
            return ResponseEntity.ok(updatedAirCraft);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Delete an aircraft by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAirCraft(@PathVariable Long id) {
        try {
            aircraftService.deleteAirCraft(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
