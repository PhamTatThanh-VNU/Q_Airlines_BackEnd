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

    /**
     * Route to CRUD
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<FlightDTO> updateFlight(@PathVariable Long id, @RequestBody FlightDTO flightDTO) {
        try {
            FlightDTO updatedFlight = flightService.updateFlight(id, flightDTO);
            return ResponseEntity.ok(updatedFlight);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/add")
    public ResponseEntity<FlightDTO> addFlight(@RequestBody FlightDTO flightDTO) {
        FlightDTO savedFlight = flightService.addFlight(flightDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFlight);
    }

    /**
     * Route search flight one way
     * @return JSON format data and error when struggle
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchFlights(
            @RequestParam String originCode,
            @RequestParam String destinationCode,
            @RequestParam LocalDate departureTime,
            @RequestParam Integer totalSeat,
            @RequestParam String ticketClass
    ) {
        try {

            List<FlightDTO> flights = flightService.getFlightDetails(originCode, destinationCode, departureTime, totalSeat, ticketClass);

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

    /**
     * Route search flight for 2 ways
     * @return JSON format data and error when struggle
     */
    @GetMapping("/searchRoundTrip")
    public ResponseEntity<?> searchFlightsRoundTrip(
            @RequestParam String originCode,
            @RequestParam String destinationCode,
            @RequestParam LocalDate departureTime,
            @RequestParam LocalDate returnTime,
            @RequestParam Integer totalSeat,
            @RequestParam String ticketClass
    ) {
        try {

            List<FlightDTO> flights = flightService.getFlightsForRoundTrip(originCode, destinationCode, departureTime, returnTime, totalSeat, ticketClass);

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
