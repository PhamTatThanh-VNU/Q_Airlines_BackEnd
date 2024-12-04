package com.example.qairlines.Controller;

import com.example.qairlines.DTO.BookingDTO;
import com.example.qairlines.Model.Booking;
import com.example.qairlines.Model.User;
import com.example.qairlines.Services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("createBooking/user/{userId}/flight/{flightId}")
    public ResponseEntity<?> createBooking(
            @PathVariable Long userId,
            @PathVariable Long flightId,
            @RequestBody BookingDTO bookingDTO) {
        Booking newBooking = bookingService.createBooking(userId, flightId, bookingDTO);
        String pdfUrl = "/pdf/" + newBooking.getBookingPdf();
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", pdfUrl).build();
    }

    @GetMapping("/pdfs")
    public List<String> getBookingPdfs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authentication.getPrincipal();

        List<String> bookingPdfs = bookingService.getAllBookingByUserId(authenticatedUser.getId());

        return bookingPdfs.stream()
                .map(bookingPdf -> "/pdf/" + bookingPdf.substring(bookingPdf.lastIndexOf('/') + 1))
                .collect(Collectors.toList());
    }
}
