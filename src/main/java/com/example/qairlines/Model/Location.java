package com.example.qairlines.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String locationName;

    @Column(nullable = false)
    private String airportName;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @OneToMany(mappedBy = "origin")
    private List<Flight> originFlights;

    @OneToMany(mappedBy = "destination")
    private List<Flight> destinationFlights;

}
