package com.rental.rentalapplication.service.impl;

import com.rental.rentalapplication.dto.request.RentRequest;
import com.rental.rentalapplication.dto.response.RentResponse;
import com.rental.rentalapplication.entity.*;
import com.rental.rentalapplication.exception.CustomException;
import com.rental.rentalapplication.exception.Error;
import com.rental.rentalapplication.mapper.RentMapper;
import com.rental.rentalapplication.repository.HouseRepository;
import com.rental.rentalapplication.repository.RentRepository;
import com.rental.rentalapplication.repository.UserRepository;
import com.rental.rentalapplication.repository.VehicleRepository;
import com.rental.rentalapplication.service.HouseService;
import com.rental.rentalapplication.service.RentService;
import com.rental.rentalapplication.service.VehicleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RentServiceImpl implements RentService {
    RentRepository rentRepository;
    HouseRepository houseRepository;
    VehicleRepository vehicleRepository;
    UserRepository userRepository;
    RentMapper rentMapper;
    VehicleService vehicleService;
    HouseService houseService;

    @PreAuthorize("hasRole('USER')")
    public RentResponse rental(RentRequest request) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new CustomException(Error.USER_NOT_EXISTED));

        Vehicle vehicle = vehicleRepository.findByNameSlug(request.getVehicle())
                .orElseThrow(() -> new CustomException(Error.VEHICLE_NOT_FOUND));
        House house = houseRepository.findByNameSlug(request.getHouse())
                .orElseThrow(() -> new CustomException(Error.VEHICLE_NOT_FOUND));

        boolean validVehicle = vehicle != null && vehicleService.checkStatus(vehicle.getId(), Status.Available.name());
        boolean validHouse = house != null && houseService.checkStatus(house.getId(), Status.Available.name());

        if (validVehicle && validHouse) {
            vehicleService.changeStatus(vehicle.getId(), Status.Rented.name());
            houseService.changeStatus(house.getId(), Status.Rented.name());
        } else if (validVehicle) {
            throw new CustomException(Error.HOUSE_RENTED);
        } else if (validHouse) {
            throw new CustomException(Error.VEHICLE_RENTED);
        } else if (!validVehicle && !validHouse) {
            throw new CustomException(Error.HOUSE_AND_VEHICEL_RENTED);
        }

        LocalDateTime now = LocalDateTime.now();
        int totalPrice = 0;

        if (vehicle != null) {
            totalPrice += calculatePrice(vehicle.getPricePerDay(), request);
        }

        if (house != null) {
            totalPrice += calculatePrice(house.getPricePerDay(), request);
        }

        Rent rent = Rent.builder()
                .vehicle(vehicle)
                .house(house)
                .user(user)
                .startTime(now)
                .endTime(now.plusHours(request.getHour()).plusDays(request.getDay()).plusMinutes(request.getMinute()))
                .totalPrice(totalPrice)
                .build();

        rentRepository.save(rent);

        return RentResponse.builder()
                .vehicle(Optional.ofNullable(vehicle).map(Vehicle::getName).orElse(null))
                .house(Optional.ofNullable(house).map(House::getName).orElse(null))
                .user(user.getUsername())
                .startTime(now)
                .totalPrice(totalPrice)
                .endTime(now.plusHours(request.getHour()).plusDays(request.getDay()).plusMinutes(request.getMinute()))
                .build();
    }


    private int calculatePrice(int pricePerDay, RentRequest request) {
        return pricePerDay * request.getDay()
                + pricePerDay / 24 * request.getHour()
                + pricePerDay / 1440 * request.getMinute();
    }


    @PreAuthorize("hasRole('ADMIN')")
    public Page<RentResponse> getAll(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<Rent> rentList = rentRepository.findAll();
        List<RentResponse> rentResponses = rentList.stream()
                .map(rentMapper::toRentResponse)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), rentResponses.size());
        List<RentResponse> sublist = rentResponses.subList(start, end);

        return new PageImpl<>(sublist, pageable, rentResponses.size());
    }

    @PreAuthorize("hasRole('USER')")
    public List<RentResponse> getMyRent() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new CustomException(Error.USER_NOT_EXISTED));

        List<Rent> rentList = rentRepository.findAllByUser(user);
        List<RentResponse> rentResponseList = new ArrayList<>();

        for (Rent rent : rentList) {

            rentResponseList.add(RentResponse.builder()
                    .user(rent.getUser().getUsername())
                    .vehicle(rent.getVehicle() != null ? rent.getVehicle().getName() : "")
                    .house(rent.getHouse() != null ? rent.getHouse().getName() : "")
                    .startTime(rent.getStartTime())
                    .endTime(rent.getEndTime())
                    .totalPrice(rent.getTotalPrice())
                    .build());
        }

        return rentResponseList;
    }

    @PreAuthorize("hasRole('USER')")
    public List<RentResponse> myExpireRent() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new CustomException(Error.USER_NOT_EXISTED));

        List<Rent> rentList = rentRepository.findAllByUser(user);
        List<RentResponse> rentResponseList = new ArrayList<>();

        for (Rent rent : rentList) {
            if (rent.getEndTime().isBefore(LocalDateTime.now())) {
                rentResponseList.add(RentResponse.builder()
                        .user(rent.getUser().getUsername())
                        .vehicle(rent.getVehicle() != null ? rent.getVehicle().getName() : "")
                        .house(rent.getHouse() != null ? rent.getHouse().getName() : "")
                        .startTime(rent.getStartTime())
                        .endTime(rent.getEndTime())
                        .totalPrice(rent.getTotalPrice())
                        .build());
            }
        }

        return rentResponseList;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<RentResponse> expireRent() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Rent> rents = rentRepository.findAllExpiredRent();

        for (Rent rent : rents
        ) {
            houseService.changeStatus(rent.getHouse().getId(), Status.Available.name());
            vehicleService.changeStatus(rent.getVehicle().getId(), Status.Available.name());
        }

        List<RentResponse> rentResponses = rents.stream()
                .map(rentMapper::toRentResponse)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), rentResponses.size());
        List<RentResponse> sublist = rentResponses.subList(start, end);

        return new PageImpl<>(sublist, pageable, rentResponses.size());
    }

}
