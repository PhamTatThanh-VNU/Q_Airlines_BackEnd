package com.example.qairlines.Services;

import com.example.qairlines.DTO.AirCraftDTO;
import com.example.qairlines.DTO.FlightDTO;
import com.example.qairlines.DTO.LocationDTO;
import com.example.qairlines.Model.AirCraft;
import com.example.qairlines.Model.Flight;
import com.example.qairlines.Model.Location;
import com.example.qairlines.Repository.FlightRepository;
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


    public List<FlightDTO> getAllFlights() {
        return flightRepository.findAll().stream().map(this::toFlightDTO).toList();
    }

    public void deleteFlight(Long id) {
        if (!flightRepository.existsById(id)) {
            throw new RuntimeException("Flight not found with id " + id);
        }
        flightRepository.deleteById(id);
    }


    /**
     * Three below function implement for converting entity to data transfer object
     *
     * @param flight is object entity pass as argument of function
     * @return DTO
     */

    private FlightDTO toFlightDTO(Flight flight) {
        return new FlightDTO(
                flight.getId(),
                flight.getFlightNumber(),
                toLocationDTO(flight.getOrigin()),
                toLocationDTO(flight.getDestination()),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getPrice(),
                flight.getAvailableEconomySeats(),
                flight.getAvailableBusinessSeats(),
                flight.getStatus(),
                toAirCraftDTO(flight.getAircraft()),
                flight.getCreatedAt()
        );
    }

    private LocationDTO toLocationDTO(Location location) {
        if (location == null) return null;
        return new LocationDTO(location.getId(), location.getLocationName(),
                location.getAirportName(), location.getCode());
    }

    private AirCraftDTO toAirCraftDTO(AirCraft aircraft) {
        if (aircraft == null) return null;
        return new AirCraftDTO(aircraft.getId(), aircraft.getAircraftCode(),
                aircraft.getManufacturer(), aircraft.getAircraftCode(), aircraft.getEconomyCapacity(), aircraft.getBusinessCapacity());
    }

    /**
     * The function for client to get flight details
     *
     * @param originCode      is code of origin location
     * @param destinationCode is code of destination location
     * @param departureTime   is time that passenger want to get start
     * @return list flight data transfer object
     */
    public List<FlightDTO> getFlightDetails(String originCode, String destinationCode, LocalDate departureTime) {
        List<FlightDTO> flights = flightRepository.getFlightDetails(originCode, destinationCode, departureTime);
        LocationDTO origin = flightRepository.getLocationForFlight(originCode);
        LocationDTO destination = flightRepository.getLocationForFlight(destinationCode);
        List<FlightDTO> flightDTOs = new ArrayList<>();

        for (FlightDTO flight : flights) {
            Long flightId = flight.getFlightId();
            String flightNumber = flight.getFlightNumber();
            LocalDateTime departureTimeField = flight.getDepartureTime();
            LocalDateTime arrivalTime = flight.getArrivalTime();
            BigDecimal price = flight.getPrice();
            Integer availableEconomySeats = flight.getAvailableEconomySeats();
            Integer availableBusinessSeats = flight.getAvailableBusinessSeats();
            Flight.Status status = flight.getStatus();
            LocalDateTime createdAt = flight.getCreatedAt();
            String aircraftCode = flight.getAirCraftCode();
            AirCraftDTO aircraft = flightRepository.getAircraft(aircraftCode);

            FlightDTO flightDTO = new FlightDTO(flightId, flightNumber, origin, destination,
                    departureTimeField, arrivalTime, price, availableEconomySeats, availableBusinessSeats, status, aircraft, createdAt);
            flightDTOs.add(flightDTO);
        }
        return flightDTOs;
    }
}

