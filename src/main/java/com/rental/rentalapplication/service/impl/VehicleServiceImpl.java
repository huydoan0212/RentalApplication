package com.rental.rentalapplication.service.impl;

import com.rental.rentalapplication.dto.request.VehicleRequest;
import com.rental.rentalapplication.dto.response.VehicleResponse;
import com.rental.rentalapplication.entity.Vehicle;
import com.rental.rentalapplication.exception.CustomException;
import com.rental.rentalapplication.exception.Error;
import com.rental.rentalapplication.mapper.VehicleMapper;
import com.rental.rentalapplication.repository.VehicleRepository;
import com.rental.rentalapplication.service.VehicleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VehicleServiceImpl implements VehicleService {
    VehicleRepository vehicleRepository;
    VehicleMapper vehicleMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public VehicleResponse create(VehicleRequest request) {
        if (vehicleRepository.existsByName(request.getName())) {
            throw new CustomException(Error.VEHICLE_EXISTED);
        }
        Vehicle vehicle = vehicleMapper.toVehicle(request);

        return vehicleMapper.toVehicleResponse(vehicleRepository.save(vehicle));
    }

    @Override
    public VehicleResponse getVehicleByNameSlug(String nameSlug) {
        return vehicleMapper.toVehicleResponse(vehicleRepository.findByNameSlug(nameSlug).orElseThrow(() -> new CustomException(Error.VEHICLE_NOT_FOUND)));
    }

    public VehicleResponse getVehicleById(String id) {
        return vehicleMapper.toVehicleResponse(vehicleRepository.findById(id).orElseThrow(() -> new CustomException(Error.VEHICLE_NOT_FOUND)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<VehicleResponse> getAll(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        var vehicles = vehicleRepository.findAll(pageable);
        return vehicles.map(vehicleMapper::toVehicleResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String id) {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new CustomException(Error.VEHICLE_NOT_FOUND));
        if (vehicle != null) {
            vehicleRepository.deleteById(id);
        }
    }

    @Override
    public boolean checkStatus(String id, String status) {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new CustomException(Error.VEHICLE_NOT_FOUND));
        return vehicle.getStatus().equals(status);
    }

    @Override
    public VehicleResponse changeStatus(String id, String status) {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new CustomException(Error.VEHICLE_NOT_FOUND));
        vehicle.setStatus(status);
        return vehicleMapper.toVehicleResponse(vehicleRepository.save(vehicle));
    }

}
