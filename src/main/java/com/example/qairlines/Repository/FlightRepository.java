package com.example.qairlines.Repository;

import com.example.qairlines.DTO.AirCraftDTO;
import com.example.qairlines.DTO.FlightDTO;
import com.example.qairlines.DTO.LocationDTO;
import com.example.qairlines.Model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT new com.example.qairlines.DTO.LocationDTO(l.locationName, l.airportName, l.code) " +
            "FROM Location l WHERE l.code = :originCode")
    LocationDTO getLocationForFlight(String originCode);

    @Query("SELECT new com.example.qairlines.DTO.AirCraftDTO(a.aircraftCode, a.manufacturer, a.model, a.seatCapacity) " +
            "FROM AirCraft a WHERE a.aircraftCode = :aircraftCode")
    AirCraftDTO getAircraft(@Param("aircraftCode") String aircraftCode);

    @Query("SELECT new com.example.qairlines.DTO.FlightDTO(f.flightNumber, f.departureTime, f.arrivalTime, f.price, f.availableSeats, f.status, f.createdAt,f.aircraft.aircraftCode)" +
            "FROM Flight f " +
            "WHERE f.origin.code = :originCode " +
            "AND f.destination.code = :destinationCode " +
            "AND DATE(f.departureTime) = :departureTime")
    List<FlightDTO> getFlightDetails(@Param("originCode") String originCode,
                                     @Param("destinationCode") String destinationCode,
                                     @Param("departureTime") LocalDate departureTime);

}
