package com.example.qairlines.Controller;

import com.example.qairlines.DTO.FlightDTO;
import com.example.qairlines.Model.Flight;
import com.example.qairlines.Model.Location;
import com.example.qairlines.Services.FlightService;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/flights")
@Data
public class FlightController {

    private final FlightService flightService;

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
