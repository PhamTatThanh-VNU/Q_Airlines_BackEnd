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
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT new com.example.qairlines.DTO.LocationDTO(l.id,l.locationName, l.airportName, l.code) " +
            "FROM Location l WHERE l.code = :originCode")
    LocationDTO getLocationForFlight(String originCode);

    @Query("SELECT new com.example.qairlines.DTO.AirCraftDTO(a.id,a.aircraftCode, a.manufacturer, a.model, a.economyCapacity,a.businessCapacity) " +
            "FROM AirCraft a WHERE a.aircraftCode = :aircraftCode")
    AirCraftDTO getAircraft(@Param("aircraftCode") String aircraftCode);

    @Query("SELECT new com.example.qairlines.DTO.FlightDTO(f.id,f.flightNumber, f.departureTime, f.arrivalTime, f.price, f.availableEconomySeats,f.availableBusinessSeats, f.status, f.createdAt,f.aircraft.aircraftCode)" +
            "FROM Flight f " +
            "WHERE f.origin.code = :originCode " +
            "AND f.destination.code = :destinationCode " +
            "AND DATE(f.departureTime) = :departureTime")
    List<FlightDTO> getFlightDetails(@Param("originCode") String originCode,
                                     @Param("destinationCode") String destinationCode,
                                     @Param("departureTime") LocalDate departureTime);
    @Query("SELECT new com.example.qairlines.DTO.FlightDTO(f.id, f.flightNumber, f.departureTime, f.arrivalTime, f.price, f.availableEconomySeats, f.availableBusinessSeats, f.status, f.createdAt, f.aircraft.aircraftCode) " +
            "FROM Flight f " +
            "WHERE (f.origin.code = :originCode AND f.destination.code = :destinationCode AND DATE(f.departureTime) = :departureDate) " +
            "OR (f.origin.code = :destinationCode AND f.destination.code = :originCode AND DATE(f.departureTime) = :returnDate)")
    List<FlightDTO> getFlightsForRoundTrip(@Param("originCode") String originCode,
                                           @Param("destinationCode") String destinationCode,
                                           @Param("departureDate") LocalDate departureDate,
                                           @Param("returnDate") LocalDate returnDate);
//    @Modifying
//    @Query("UPDATE Flight f SET f.availableSeats = f.availableSeats - :totalPeople WHERE f.id = :flightId")
//    void updateAvailableSeats(@Param("flightId") Long flightId, @Param("totalPeople") Integer totalPeople);

}
