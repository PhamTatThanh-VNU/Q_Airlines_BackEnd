package com.example.qairlines.Controller;

import com.example.qairlines.DTO.LocationDTO;
import com.example.qairlines.Services.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/all")
    public ResponseEntity<List<LocationDTO>> getAllLocation() {
        try {
            List<LocationDTO> locations = locationService.getAllLocation();
            if (locations.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(locations);
            }
            return ResponseEntity.status(HttpStatus.OK).body(locations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
