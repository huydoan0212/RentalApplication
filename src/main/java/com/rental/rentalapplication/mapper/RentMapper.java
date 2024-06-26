package com.rental.rentalapplication.mapper;

import com.rental.rentalapplication.dto.response.RentResponse;
import com.rental.rentalapplication.entity.Rent;
import com.rental.rentalapplication.repository.UserRepository;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public abstract class RentMapper {

    public RentResponse toRentResponse(Rent rent) {
        return RentResponse.builder()
                .user(rent.getUser() != null ? rent.getUser().getUsername() : "")
                .vehicle(rent.getVehicle() != null ? rent.getVehicle().getName() : "")
                .house(rent.getHouse() != null ? rent.getHouse().getName() : "")
                .startTime(rent.getStartTime())
                .endTime(rent.getEndTime())
                .totalPrice(rent.getTotalPrice())
                .build();
    }
}

