package com.example.qairlines.Repository;

import com.example.qairlines.DTO.BookingDTO;
import com.example.qairlines.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("Select b.bookingPdf from Booking as b where b.user.id = :userId")
    List<String> getAllBookingByUserId(Long userId);
}
