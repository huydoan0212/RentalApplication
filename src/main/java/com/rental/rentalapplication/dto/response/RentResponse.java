package com.rental.rentalapplication.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RentResponse {
    String user;
    String vehicle;
    String house;
    LocalDateTime startTime;
    LocalDateTime endTime;
    int totalPrice;

}
