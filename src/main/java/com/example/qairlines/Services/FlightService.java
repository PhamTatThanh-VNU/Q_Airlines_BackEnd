package com.example.qairlines.Services;

import com.example.qairlines.Model.Flight;
import com.example.qairlines.Repository.FlightRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class FlightService {
    private FlightRepository flightRepository;
    public List<Flight> getAllFlight(){
        return flightRepository.findAll();
    }
}
