package com.rental.rentalapplication.controller;

import com.rental.rentalapplication.dto.request.HouseRequest;
import com.rental.rentalapplication.dto.response.ApiResponse;
import com.rental.rentalapplication.dto.response.HouseResponse;
import com.rental.rentalapplication.service.impl.HouseServiceImple;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/houses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "House")
public class HouseController {
    HouseServiceImple houseServiceImple;

    @PostMapping
    ApiResponse<HouseResponse> create(@RequestBody HouseRequest request) {
        HouseResponse response = houseServiceImple.create(request);
        return ApiResponse.<HouseResponse>builder()
                .message("House created")
                .status(response != null ? "success" : "failure")
                .timeStamp(LocalDateTime.now())
                .responseData(response)
                .build();
    }

    @GetMapping
    ApiResponse<Page<HouseResponse>> getAll(Integer pageNo, Integer pageSize) {
        Page<HouseResponse> responses = houseServiceImple.getAll(pageNo, pageSize);
        return ApiResponse.<Page<HouseResponse>>builder()
                .message("Get all house")
                .status(responses != null ? "success" : "failure")
                .timeStamp(LocalDateTime.now())
                .responseData(responses)
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> delete(@PathVariable("id") String id) {
        houseServiceImple.delete(id);
        return ApiResponse.<Void>builder()
                .message("Deleted")
                .status("success")
                .timeStamp(LocalDateTime.now())
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<HouseResponse> update(@RequestBody HouseRequest request, @PathVariable("id") String id) {
        HouseResponse response = houseServiceImple.updateHouse(id, request);
        return ApiResponse.<HouseResponse>builder()
                .message("Updated")
                .status(response != null ? "success" : "failure")
                .responseData(response)
                .timeStamp(LocalDateTime.now())
                .build();
    }

    @GetMapping("/id/{id}")
    ApiResponse<HouseResponse> getById(@PathVariable("id") String id) {
        HouseResponse response = houseServiceImple.getHouseById(id);
        return ApiResponse.<HouseResponse>builder()
                .message("Get house by id")
                .status(response != null ? "success" : "failure")
                .timeStamp(LocalDateTime.now())
                .responseData(response)
                .build();
    }

    @GetMapping("/nameSlug/{nameSlug}")
    ApiResponse<HouseResponse> getNameSlug(@PathVariable("nameSlug") String nameSlug) {
        HouseResponse response = houseServiceImple.getHouse(nameSlug);
        return ApiResponse.<HouseResponse>builder()
                .message("Get house by nameSlug")
                .status(response != null ? "success" : "failure")
                .timeStamp(LocalDateTime.now())
                .responseData(response)
                .build();
    }
}
