package com.example.qairlines.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirCraftDTO {
    private String aircraftCode;
    private String manufacturer;
    private String model;
    private int seatCapacity;
}
