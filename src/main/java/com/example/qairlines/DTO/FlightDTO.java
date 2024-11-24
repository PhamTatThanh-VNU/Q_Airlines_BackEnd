package com.example.qairlines.DTO;

import com.example.qairlines.Model.AirCraft;
import com.example.qairlines.Model.Flight;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FlightDTO {
    private String flightNumber;
    private LocationDTO origin;
    private LocationDTO destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private BigDecimal price;
    private int availableSeats;
    private Flight.Status status;
    private AirCraftDTO aircraft;
    @JsonIgnore private String airCraftCode;
    private LocalDateTime createdAt;

    public FlightDTO(String flightNumber, LocalDateTime departureTime, LocalDateTime arrivalTime, BigDecimal price, int availableSeats, Flight.Status status, LocalDateTime createdAt, String airCraftCode) {
        this.flightNumber = flightNumber;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.availableSeats = availableSeats;
        this.status = status;
        this.createdAt = createdAt;
        this.airCraftCode = airCraftCode;
    }

    public FlightDTO(String flightNumber, LocationDTO origin, LocationDTO destination, LocalDateTime departureTime, LocalDateTime arrivalTime, BigDecimal price, int availableSeats, Flight.Status status, AirCraftDTO aircraft, LocalDateTime createdAt) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.availableSeats = availableSeats;
        this.status = status;
        this.aircraft = aircraft;
        this.createdAt = createdAt;
    }
}
