package com.rental.rentalapplication.service;

import com.rental.rentalapplication.dto.request.RentRequest;
import com.rental.rentalapplication.dto.response.RentResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RentService {
    RentResponse rental(RentRequest request);

    Page<RentResponse> getAll(Integer pageNo, Integer pageSize);

    List<RentResponse> getMyRent();

    List<RentResponse> myExpireRent();

    Page<RentResponse> expireRent();
}
