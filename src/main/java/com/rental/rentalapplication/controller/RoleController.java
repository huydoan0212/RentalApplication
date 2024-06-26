package com.rental.rentalapplication.controller;

import com.rental.rentalapplication.dto.request.RoleRequest;
import com.rental.rentalapplication.dto.response.ApiResponse;
import com.rental.rentalapplication.dto.response.RoleResponse;
import com.rental.rentalapplication.service.impl.RoleServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Role")
public class RoleController {
    RoleServiceImpl roleServiceImpl;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        RoleResponse roleResponse = roleServiceImpl.create(request);
        return ApiResponse.<RoleResponse>builder()
                .message("Role created")
                .status(roleResponse != null ? "success" : "failure")
                .timeStamp(LocalDateTime.now())
                .responseData(roleResponse)
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        List<RoleResponse> roles = roleServiceImpl.getAll();
        return ApiResponse.<List<RoleResponse>>builder()
                .message("Role created")
                .status(roles != null ? "success" : "failure")
                .timeStamp(LocalDateTime.now())
                .responseData(roles)
                .build();
    }

    @DeleteMapping("/{role}")
    ApiResponse<Void> delete(@PathVariable("role") String role) {
        roleServiceImpl.delete(role);
        return ApiResponse.<Void>builder()
                .status("success")
                .build();
    }
}
