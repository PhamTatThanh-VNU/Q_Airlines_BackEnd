package com.example.qairlines.Repository;

import com.example.qairlines.DTO.BookingDTO.BookingResponseDTO;
import com.example.qairlines.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("Select new com.example.qairlines.DTO.BookingDTO.BookingResponseDTO" +
            "(b.id,b.flight.flightNumber,b.flight.departureTime,b.flight.arrivalTime,b.flight.price,b.bookingNumber,b.email,b.phoneNumber,b.status,b.totalPeople,b.bookingPdf) " +
            "from Booking as b where b.user.id = :userId")
    List<BookingResponseDTO> getAllBookingByUserId(Long userId);
}

