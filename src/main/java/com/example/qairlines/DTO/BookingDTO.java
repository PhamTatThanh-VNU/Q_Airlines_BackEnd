package com.example.qairlines.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private String passengerName;
    private String email;
    private String phoneNumber;
    private Long totalPrices;
    private Integer totalPeople;
}
