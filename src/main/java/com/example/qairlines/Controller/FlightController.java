package com.example.qairlines.Controller;

import com.example.qairlines.DTO.FlightDTO;
import com.example.qairlines.Services.FlightService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    // 1. Get all flights
    @GetMapping("/all")
    public ResponseEntity<List<FlightDTO>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    // 5. Delete a flight
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchFlights(
            @RequestParam String originCode,
            @RequestParam String destinationCode,
            @RequestParam LocalDate departureTime
    ) {
        try {

            List<FlightDTO> flights = flightService.getFlightDetails(originCode, destinationCode, departureTime);

            if (flights.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No flights found matching the given criteria.");
            }

            return ResponseEntity.ok(flights);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while searching for flights: " + e.getMessage());
        }
    }
}
