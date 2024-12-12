package com.example.qairlines.DTO.BookingDTO;

import com.example.qairlines.DTO.FlightDTO;
import com.example.qairlines.Model.Booking;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDTO {
    private Long bookingId;
    private String flightNumber;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private BigDecimal price;
    private String bookingNumber;
    private String email;
    private String phoneNumber;
    private Booking.Status status;
    private Integer totalPeople;
    private String pdfs;

}
