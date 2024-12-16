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

    /**
     * The below function to CRUD flight
     */
    public FlightDTO updateFlight(Long id, FlightDTO flightDTO) {
        Flight existingFlight = flightRepository.findById(id).orElseThrow(() -> new RuntimeException("Flight not found with id " + id));
        //Update
        existingFlight.setFlightNumber(flightDTO.getFlightNumber());
        existingFlight.setOrigin(toLocationEntity(flightDTO.getOrigin()));
        existingFlight.setDestination(toLocationEntity(flightDTO.getDestination()));
        existingFlight.setDepartureTime(flightDTO.getDepartureTime());
        existingFlight.setArrivalTime(flightDTO.getArrivalTime());
        existingFlight.setPrice(flightDTO.getPrice());
        existingFlight.setAvailableEconomySeats(flightDTO.getAvailableEconomySeats());
        existingFlight.setAvailableBusinessSeats(flightDTO.getAvailableBusinessSeats());
        existingFlight.setStatus(flightDTO.getStatus());
        existingFlight.setAircraft(toAirCraftEntity(flightDTO.getAircraft()));
        //Save
        Flight updatedFlight = flightRepository.save(existingFlight);
        //Return update
        return toFlightDTO(updatedFlight);
    }

    public void deleteFlight(Long id) {
        if (!flightRepository.existsById(id)) {
            throw new RuntimeException("Flight not found with id " + id);
        }
        flightRepository.deleteById(id);
    }

    public FlightDTO addFlight(FlightDTO flightDTO) {
        // Convert DTO to Entity
        Flight flight = new Flight();
        flight.setFlightNumber(flightDTO.getFlightNumber());
        flight.setOrigin(toLocationEntity(flightDTO.getOrigin()));
        flight.setDestination(toLocationEntity(flightDTO.getDestination()));
        flight.setDepartureTime(flightDTO.getDepartureTime());
        flight.setArrivalTime(flightDTO.getArrivalTime());
        flight.setPrice(flightDTO.getPrice());
        flight.setAvailableEconomySeats(flightDTO.getAvailableEconomySeats());
        flight.setAvailableBusinessSeats(flightDTO.getAvailableBusinessSeats());
        flight.setStatus(flightDTO.getStatus());
        flight.setAircraft(toAirCraftEntity(flightDTO.getAircraft()));
        flight.setCreatedAt(LocalDateTime.now());

        // Save to the database
        Flight savedFlight = flightRepository.save(flight);

        // Return the saved flight as DTO
        return toFlightDTO(savedFlight);
    }

    /**
     * The below function for convert DTO to entity
     */
    private Location toLocationEntity(LocationDTO locationDTO) {
        if (locationDTO == null) return null;
        Location location = new Location();
        location.setId(locationDTO.getId());
        location.setLocationName(locationDTO.getLocationName());
        location.setAirportName(locationDTO.getAirportName());
        location.setCode(locationDTO.getCode());
        return location;
    }

    private AirCraft toAirCraftEntity(AirCraftDTO airCraftDTO) {
        if (airCraftDTO == null) return null;
        AirCraft aircraft = new AirCraft();
        aircraft.setId(airCraftDTO.getId());
        aircraft.setAircraftCode(airCraftDTO.getAircraftCode());
        aircraft.setManufacturer(airCraftDTO.getManufacturer());
        aircraft.setEconomyCapacity(airCraftDTO.getEconomyCapacity());
        aircraft.setBusinessCapacity(airCraftDTO.getBusinessCapacity());
        return aircraft;
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
    public List<FlightDTO> getFlightDetails(String originCode, String destinationCode, LocalDate departureTime, Integer totalSeat, String ticketClass) {
        //Get location to save flightDTO
        List<FlightDTO> flights = flightRepository.getFlightDetails(originCode, destinationCode, departureTime);
        LocationDTO origin = flightRepository.getLocationForFlight(originCode);
        LocationDTO destination = flightRepository.getLocationForFlight(destinationCode);
        List<FlightDTO> flightDTOs = new ArrayList<>();
        // Loop for flightDTO and check
        for (FlightDTO flight : flights) {
            Integer availableEconomySeats = flight.getAvailableEconomySeats();
            Integer availableBusinessSeats = flight.getAvailableBusinessSeats();
            //Check available seat
            boolean isAvailable = false;
            if ("economy".equalsIgnoreCase(ticketClass)) {
                isAvailable = availableEconomySeats >= totalSeat;
            } else if ("business".equalsIgnoreCase(ticketClass)) {
                isAvailable = availableBusinessSeats >= totalSeat;
            }
            if (isAvailable) {
                Long flightId = flight.getFlightId();
                String flightNumber = flight.getFlightNumber();
                LocalDateTime departureTimeField = flight.getDepartureTime();
                LocalDateTime arrivalTime = flight.getArrivalTime();
                BigDecimal price = flight.getPrice();

                Flight.Status status = flight.getStatus();
                LocalDateTime createdAt = flight.getCreatedAt();
                String aircraftCode = flight.getAirCraftCode();
                AirCraftDTO aircraft = flightRepository.getAircraft(aircraftCode);

                FlightDTO flightDTO = new FlightDTO(flightId, flightNumber, origin, destination,
                        departureTimeField, arrivalTime, price, availableEconomySeats, availableBusinessSeats, status, aircraft, createdAt);
                flightDTOs.add(flightDTO);
            }
        }
        return flightDTOs;
    }

    /**
     * The function for client  want to get flight details for round trip
     *
     * @param originCode      is code of origin location
     * @param destinationCode is code of destination location
     * @param departureTime   is time that passenger want to get start
     * @param returnTime      is time that passenger want to return
     * @return list flight data transfer object
     */
    public List<FlightDTO> getFlightsForRoundTrip(String originCode, String destinationCode, LocalDate departureTime, LocalDate returnTime, Integer totalSeat, String ticketClass) {
        List<FlightDTO> flights = flightRepository.getFlightsForRoundTrip(originCode, destinationCode, departureTime, returnTime);
        LocationDTO origin = flightRepository.getLocationForFlight(originCode);
        LocationDTO destination = flightRepository.getLocationForFlight(destinationCode);

        List<FlightDTO> flightDTOs = new ArrayList<>();
        // Loop for flightDTO and check
        for (FlightDTO flight : flights) {
            Integer availableEconomySeats = flight.getAvailableEconomySeats();
            Integer availableBusinessSeats = flight.getAvailableBusinessSeats();
            //Check available seat
            boolean isAvailable = false;
            if ("economy".equalsIgnoreCase(ticketClass)) {
                isAvailable = availableEconomySeats >= totalSeat;
            } else if ("business".equalsIgnoreCase(ticketClass)) {
                isAvailable = availableBusinessSeats >= totalSeat;
            }
            if (isAvailable) {
                Long flightId = flight.getFlightId();
                String flightNumber = flight.getFlightNumber();
                LocalDateTime departureTimeField = flight.getDepartureTime();
                LocalDateTime arrivalTime = flight.getArrivalTime();
                BigDecimal price = flight.getPrice();

                Flight.Status status = flight.getStatus();
                LocalDateTime createdAt = flight.getCreatedAt();
                String aircraftCode = flight.getAirCraftCode();
                AirCraftDTO aircraft = flightRepository.getAircraft(aircraftCode);

                FlightDTO flightDTO = new FlightDTO(flightId, flightNumber, origin, destination,
                        departureTimeField, arrivalTime, price, availableEconomySeats, availableBusinessSeats, status, aircraft, createdAt);
                flightDTOs.add(flightDTO);
            }
        }
        return flightDTOs;
    }
}

