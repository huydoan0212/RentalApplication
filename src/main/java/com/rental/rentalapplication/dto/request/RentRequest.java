package com.rental.rentalapplication.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RentRequest {
    String vehicle;
    String house;
    int day;
    int hour;
    int minute;
}
