package com.rental.rentalapplication.service;

import com.rental.rentalapplication.dto.request.VehicleRequest;
import com.rental.rentalapplication.dto.response.VehicleResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VehicleService {
    VehicleResponse create(VehicleRequest request);

    VehicleResponse getVehicleById(String id);
    VehicleResponse getVehicleByNameSlug(String nameSlug);
    Page<VehicleResponse> getAll(Integer pageNo, Integer pageSize);

    void delete(String name);

    boolean checkStatus(String id, String status);

    VehicleResponse changeStatus(String id, String status);
}
