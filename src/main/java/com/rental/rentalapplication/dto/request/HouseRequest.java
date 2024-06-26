package com.rental.rentalapplication.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HouseRequest {
    String name;
    int pricePerDay;
    String description;
    String status;
}
