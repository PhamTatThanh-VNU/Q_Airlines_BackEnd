package com.example.qairlines.DTO.BookingDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingSubmitDTO {
    private String passengerName;
    private String email;
    private String phoneNumber;
    private Long totalPrices;
    private Integer totalPeople;
}
