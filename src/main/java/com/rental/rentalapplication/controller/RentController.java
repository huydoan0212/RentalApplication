package com.rental.rentalapplication.controller;

import com.rental.rentalapplication.dto.request.RentRequest;
import com.rental.rentalapplication.dto.response.ApiResponse;
import com.rental.rentalapplication.dto.response.RentResponse;
import com.rental.rentalapplication.service.impl.RentServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/rental")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Rent")
public class RentController {
    RentServiceImpl rentServiceImpl;

    @PostMapping
    public ApiResponse<RentResponse> rent(@RequestBody RentRequest request) {
        RentResponse response = rentServiceImpl.rental(request);
        return ApiResponse.<RentResponse>builder()
                .message("Rent successfully")
                .status(response != null ? "success" : "failure")
                .timeStamp(LocalDateTime.now())
                .responseData(response)
                .build();
    }

    @GetMapping
    public ApiResponse<Page<RentResponse>> getAll(Integer pageNo, Integer pageSize) {
        Page<RentResponse> rentPage = rentServiceImpl.getAll(pageNo, pageSize);
        return ApiResponse.<Page<RentResponse>>builder()
                .message("Get all rent successfully")
                .status(rentPage != null ? "success" : "failure")
                .timeStamp(LocalDateTime.now())
                .responseData(rentPage)
                .build();
    }

    @GetMapping("/myListRent")
    public ApiResponse<List<RentResponse>> getMyRent() {
        List<RentResponse> responses = rentServiceImpl.getMyRent();
        return ApiResponse.<List<RentResponse>>builder()
                .message("Get my list rent successfully")
                .status(responses != null ? "success" : "failed")
                .timeStamp(LocalDateTime.now())
                .responseData(responses)
                .build();
    }

    @GetMapping("/myExpireRent")
    public ApiResponse<List<RentResponse>> myExpireRent() {
        List<RentResponse> responses = rentServiceImpl.myExpireRent();
        return ApiResponse.<List<RentResponse>>builder()
                .message("Get expire rent successfully")
                .status(responses != null ? "success" : "failed")
                .timeStamp(LocalDateTime.now())
                .responseData(responses)
                .build();
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    @GetMapping("/expireRent")
    public ApiResponse<Page<RentResponse>> expireRent() {
        Page<RentResponse> rentPage = rentServiceImpl.expireRent();
        String status = rentPage != null ? "success" : "failed";

        ApiResponse<Page<RentResponse>> response = ApiResponse.<Page<RentResponse>>builder()
                .message("Get my list rent successfully")
                .status(status)
                .timeStamp(LocalDateTime.now())
                .responseData(rentPage)
                .build();

        log.info("Expire Rent List: " + response);
        return response;
    }


}
