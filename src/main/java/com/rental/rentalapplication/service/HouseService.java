package com.rental.rentalapplication.service;

import com.rental.rentalapplication.dto.request.HouseRequest;
import com.rental.rentalapplication.dto.response.HouseResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface HouseService {
    HouseResponse create(HouseRequest request);

    HouseResponse getHouse(String name);

    HouseResponse updateHouse(String id, HouseRequest request);

    Page<HouseResponse> getAll(Integer pageNo, Integer pageSize);

    void delete(String name);

    HouseResponse getHouseById(String id);

    boolean checkStatus(String id, String status);

    HouseResponse changeStatus(String id, String status);
}
