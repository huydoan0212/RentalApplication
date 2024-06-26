package com.rental.rentalapplication.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HouseResponse {
    String name;
    int pricePerDay;
    String description;
    String status;
}
