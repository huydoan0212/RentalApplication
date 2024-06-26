package com.rental.rentalapplication.mapper;

import com.rental.rentalapplication.dto.request.HouseRequest;
import com.rental.rentalapplication.dto.request.VehicleRequest;
import com.rental.rentalapplication.dto.response.HouseResponse;
import com.rental.rentalapplication.dto.response.VehicleResponse;
import com.rental.rentalapplication.entity.House;
import com.rental.rentalapplication.entity.Vehicle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    Vehicle toVehicle(VehicleRequest request);

    VehicleResponse toVehicleResponse(Vehicle vehicle);
}
