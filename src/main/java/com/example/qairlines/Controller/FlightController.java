package com.example.qairlines.Controller;

import com.example.qairlines.Model.Flight;
import com.example.qairlines.Model.Location;
import com.example.qairlines.Services.FlightService;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/flights")
@Data
public class FlightController {

    private final FlightService flightService;

    @GetMapping("/allFlight")
    public List<Flight> getAllFlights() {
        return flightService.getAllFlight();
    }
}
