package com.example.qairlines.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Column(name = "booking_number", nullable = false, unique = true, length = 20, updatable = false)
    private String bookingNumber;

    @Column(name = "passenger_name", nullable = false)
    private String passengerName;

    @Column(name = "passenger_email", nullable = false)
    private String email;

    @Column(name = "passenger_phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "booking_pdf", length = 255)
    private String bookingPdf;
    @Column(name = "ticket_class", length = 255)
    private String ticketClass;
    @Column(name = "total_prices", length = 255)
    private Long totalPrices;
    @Column(name = "total_people")
    private Integer totalPeople;
    public enum Status {
        PENDING,
        CONFIRMED,
        CANCELLED
    }

}
