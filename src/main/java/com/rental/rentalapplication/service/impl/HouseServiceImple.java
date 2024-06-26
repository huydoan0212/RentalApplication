package com.rental.rentalapplication.service.impl;

import com.rental.rentalapplication.dto.request.HouseRequest;
import com.rental.rentalapplication.dto.response.HouseResponse;
import com.rental.rentalapplication.entity.House;
import com.rental.rentalapplication.exception.CustomException;
import com.rental.rentalapplication.exception.Error;
import com.rental.rentalapplication.mapper.HouseMapper;
import com.rental.rentalapplication.repository.HouseRepository;
import com.rental.rentalapplication.service.HouseService;
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
public class HouseServiceImple implements HouseService {
    HouseRepository houseRepository;
    HouseMapper houseMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public HouseResponse create(HouseRequest request) {
        if (houseRepository.existsByName(request.getName())) {
            throw new CustomException(Error.HOUSE_EXISTED);
        }
        House house = houseMapper.toHouse(request);

        return houseMapper.toHouseResponse(houseRepository.save(house));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public HouseResponse getHouse(String name) {
        return houseMapper.toHouseResponse(houseRepository.findByNameSlug(name).orElseThrow(() -> new CustomException(Error.HOUSE_NOT_FOUND)));
    }

    @Override
    public HouseResponse updateHouse(String id, HouseRequest request) {
        House house = houseRepository.findById(id).orElseThrow(() -> new CustomException(Error.HOUSE_NOT_FOUND));
        house.setName(request.getName());
        house.setDescription(request.getDescription());
        house.setStatus(request.getStatus());
        house.setPricePerDay(request.getPricePerDay());

        return houseMapper.toHouseResponse(houseRepository.save(house));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<HouseResponse> getAll(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        var houses = houseRepository.findAll(pageable);
        return houses.map(houseMapper::toHouseResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String id) {
        House house = houseRepository.findById(id).orElseThrow(() -> new CustomException(Error.HOUSE_NOT_FOUND));
        if (house != null) {
            houseRepository.deleteById(id);
        }
    }

    @Override
    public HouseResponse getHouseById(String id) {
        return houseMapper.toHouseResponse(houseRepository.findById(id).orElseThrow(() -> new CustomException(Error.HOUSE_NOT_FOUND)));
    }

    @Override
    public boolean checkStatus(String id, String status) {
        House vehicle = houseRepository.findById(id).orElseThrow(() -> new CustomException(Error.VEHICLE_NOT_FOUND));
        return vehicle.getStatus().equals(status);
    }

    @Override
    public HouseResponse changeStatus(String id, String status) {
        House house = houseRepository.findById(id).orElseThrow(() -> new CustomException(Error.HOUSE_NOT_FOUND));
        house.setStatus(status);
        return houseMapper.toHouseResponse(houseRepository.save(house));
    }

}
