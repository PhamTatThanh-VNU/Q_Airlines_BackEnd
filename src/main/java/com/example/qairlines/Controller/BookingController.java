package com.example.qairlines.Controller;

import com.example.qairlines.DTO.BookingDTO.BookingResponseDTO;
import com.example.qairlines.DTO.BookingDTO.BookingSubmitDTO;
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
            @RequestBody BookingSubmitDTO bookingDTO) {
        bookingService.createBooking(userId, flightId, bookingDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Đặt vé thành công, bạn có thể vào phần quản lý vé để xem vé đã đặt");
    }

    @GetMapping("/bookingInformation")
    public List<BookingResponseDTO> getBookingPdfs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authentication.getPrincipal();
        return bookingService.getAllBookingByUserId(authenticatedUser.getId());
    }
}
