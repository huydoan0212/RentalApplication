package com.rental.rentalapplication.mapper;

import com.rental.rentalapplication.dto.request.HouseRequest;
import com.rental.rentalapplication.dto.response.HouseResponse;
import com.rental.rentalapplication.entity.House;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HouseMapper {
    House toHouse(HouseRequest request);

    HouseResponse toHouseResponse(House house);
}
