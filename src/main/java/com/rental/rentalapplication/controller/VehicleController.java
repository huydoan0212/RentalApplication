package com.rental.rentalapplication.controller;

import com.rental.rentalapplication.dto.request.VehicleRequest;
import com.rental.rentalapplication.dto.response.ApiResponse;
import com.rental.rentalapplication.dto.response.VehicleResponse;
import com.rental.rentalapplication.service.impl.VehicleServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Vehicle")
@PreAuthorize("hasRole('ADMIN')")
public class VehicleController {
    VehicleServiceImpl vehicleServiceImpl;

    @PostMapping
    ApiResponse<VehicleResponse> create(@RequestBody VehicleRequest request) {
        VehicleResponse vehicleResponse = vehicleServiceImpl.create(request);
        return ApiResponse.<VehicleResponse>builder()
                .message("Create vehicle")
                .status(vehicleResponse != null ? "success" : "failure")
                .timeStamp(LocalDateTime.now())
                .responseData(vehicleResponse)
                .build();
    }

    @GetMapping
    ApiResponse<Page<VehicleResponse>> getAll(Integer pageNo, Integer pageSize) {
        Page<VehicleResponse> responses = vehicleServiceImpl.getAll(pageNo, pageSize);
        return ApiResponse.<Page<VehicleResponse>>builder()
                .message("Get all vehicle")
                .status(responses != null ? "success" : "failure")
                .timeStamp(LocalDateTime.now())
                .responseData(responses)
                .build();
    }

    @DeleteMapping("/{vehicle}")
    ApiResponse<Void> delete(@PathVariable("vehicle") String id) {
        vehicleServiceImpl.delete(id);
        return ApiResponse.<Void>builder().message("Deleted vehicle").build();
    }

    @GetMapping("/id/{id}")
    ApiResponse<VehicleResponse> getById(@PathVariable("id") String id) {
        VehicleResponse vehicleResponse = vehicleServiceImpl.getVehicleById(id);
        return ApiResponse.<VehicleResponse>builder()
                .message("Get vehicle by id")
                .status(vehicleResponse != null ? "success" : "failure")
                .timeStamp(LocalDateTime.now())
                .responseData(vehicleResponse)
                .build();
    }

    @GetMapping("/nameSlug/{nameSlug}")
    ApiResponse<VehicleResponse> getByNameSlug(@PathVariable("nameSlug") String nameSlug) {
        VehicleResponse vehicleResponse = vehicleServiceImpl.getVehicleByNameSlug(nameSlug);
        return ApiResponse.<VehicleResponse>builder()
                .message("Get vehicle by name slug")
                .status(vehicleResponse != null ? "success" : "failure")
                .timeStamp(LocalDateTime.now())
                .responseData(vehicleResponse)
                .build();
    }
}
