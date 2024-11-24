package com.example.qairlines.Services;

import com.example.qairlines.DTO.AirCraftDTO;
import com.example.qairlines.DTO.FlightDTO;
import com.example.qairlines.DTO.LocationDTO;
import com.example.qairlines.Model.Flight;
import com.example.qairlines.Repository.FlightRepository;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    public List<FlightDTO> getFlightDetails(String originCode, String destinationCode, LocalDate departureTime) {
        List<FlightDTO> flights = flightRepository.getFlightDetails(originCode, destinationCode, departureTime);
        System.out.println(flights);
        LocationDTO origin = flightRepository.getLocationForFlight(originCode);
        LocationDTO destination = flightRepository.getLocationForFlight(destinationCode);
        List<FlightDTO> flightDTOs = new ArrayList<>();

        for (FlightDTO flight : flights) {
            String flightNumber = flight.getFlightNumber();
            LocalDateTime departureTimeField = flight.getDepartureTime();
            LocalDateTime arrivalTime = flight.getArrivalTime();
            BigDecimal price = flight.getPrice();
            int availableSeats = flight.getAvailableSeats();
            Flight.Status status = flight.getStatus();
            LocalDateTime createdAt = flight.getCreatedAt();
            String aircraftCode = flight.getAirCraftCode();
            AirCraftDTO aircraft = flightRepository.getAircraft(aircraftCode);
            System.out.println(aircraft.getAircraftCode());


            FlightDTO flightDTO = new FlightDTO(flightNumber, origin, destination, departureTimeField, arrivalTime, price, availableSeats, status, aircraft, createdAt);
            flightDTOs.add(flightDTO);
        }
        return flightDTOs;
    }
}

