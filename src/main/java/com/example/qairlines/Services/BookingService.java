package com.example.qairlines.Services;

import com.example.qairlines.DTO.BookingDTO.BookingResponseDTO;
import com.example.qairlines.DTO.BookingDTO.BookingSubmitDTO;
import com.example.qairlines.Model.Booking;
import com.example.qairlines.Model.Flight;
import com.example.qairlines.Model.User;
import com.example.qairlines.Repository.BookingRepository;
import com.example.qairlines.Repository.FlightRepository;
import com.example.qairlines.Repository.UserRepository;
import com.itextpdf.text.pdf.BaseFont;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * The function for create new booking
     * @param userId id of user want to create new booking
     * @param flightId id of flight user want to book
     * @param bookingDTO data transfer object booking
     * @return new Booking just create
     */
    @Transactional
    public Booking createBooking(Long userId, Long flightId, BookingSubmitDTO bookingDTO) {
        Booking newBooking = new Booking();
        User authUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Flight flight = flightRepository.findById(flightId).orElseThrow(() -> new RuntimeException("Flight not found with id: " + flightId));
        String bookingNumber = generateBookingNumber();
        if (bookingDTO.getTotalPeople() > flight.getAvailableBusinessSeats() + flight.getAvailableEconomySeats()) {
            return null;
        }
        //Save booking
        newBooking.setUser(authUser);
        newBooking.setFlight(flight);
        newBooking.setBookingNumber(bookingNumber);
        newBooking.setEmail(bookingDTO.getEmail());
        newBooking.setPassengerName(bookingDTO.getPassengerName());
        newBooking.setPhoneNumber(bookingDTO.getPhoneNumber());
        newBooking.setTicketClass(bookingDTO.getTicketClass());
        newBooking.setTotalPrices(bookingDTO.getTotalPrices());
        newBooking.setTotalPeople(bookingDTO.getTotalPeople());
        // Try to create pdf from html
        try {
            String htmlContent = generateHtmlContent(newBooking);
            String pdfFileName = generatePdf(newBooking, htmlContent);
            newBooking.setBookingPdf(pdfFileName);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF for booking: " + bookingNumber, e);
        }

        return bookingRepository.save(newBooking);
    }



    @Transactional
    public Booking confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking with id " + bookingId + " not found"));

        if (booking.getStatus() != Booking.Status.PENDING) {
            throw new IllegalStateException("Only PENDING bookings can be confirmed.");
        }

        booking.setStatus(Booking.Status.CONFIRMED);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking with id " + bookingId + " not found"));

        if (booking.getStatus() != Booking.Status.PENDING) {
            throw new IllegalStateException("Only PENDING bookings can be cancelled.");
        }

        booking.setStatus(Booking.Status.CANCELLED);
        return bookingRepository.save(booking);
    }


    /**
     * Extension function to generate pdf file
     */
    private String generateBookingNumber() {
        return "BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateHtmlContent(Booking booking) {
        Context context = new Context();
        context.setVariable("booking", booking);
        context.setVariable("departureTime", formatDateTime(booking.getFlight().getDepartureTime()));
        context.setVariable("arrivalTime", formatDateTime(booking.getFlight().getArrivalTime()));
        return templateEngine.process("pdf", context);
    }

    // Create pdf from htmlW
    private String generatePdf(Booking booking, String htmlContent) throws Exception {
        Path bookingsDir = Paths.get("src/main/resources/static/pdf");

        if (!Files.exists(bookingsDir)) {
            Files.createDirectory(bookingsDir);
        }
        String pdfFileName = "Booking_" + booking.getBookingNumber() + ".pdf";
        Path pdfPath = bookingsDir.resolve(pdfFileName);

        try (OutputStream os = new FileOutputStream(pdfPath.toFile())) {
            ITextRenderer renderer = new ITextRenderer();
            ClassPathResource fontResource = new ClassPathResource("static/font/DejaVuSans.ttf");
            renderer.getFontResolver().addFont(
                    fontResource.getFile().getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED
            );
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(os);
        }

        return pdfFileName;
    }

    public String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        return dateTime.format(formatter);
    }

    /**
     * Function to get all booking by one user
     * @param userId is id of user
     * @return details information include flight information, location information and booking information
     */
    public List<BookingResponseDTO> getAllBookingByUserId(Long userId) {
        return bookingRepository.getAllBookingByUserId(userId);
    }
    /**
     * Function to get all booking for admin
     */
    public List<BookingResponseDTO> getAllBooking() {
        return bookingRepository.getAllBooking();
    }
    public void deleteBookingByStatus(Booking.Status status) {
        bookingRepository.deleteBookingsByStatus(status);
    }
}
