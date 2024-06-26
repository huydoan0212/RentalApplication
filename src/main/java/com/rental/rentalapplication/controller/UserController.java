package com.rental.rentalapplication.controller;


import com.rental.rentalapplication.dto.request.UserCreationRequest;
import com.rental.rentalapplication.dto.request.UserUpdateRequest;
import com.rental.rentalapplication.dto.response.ApiResponse;
import com.rental.rentalapplication.dto.response.UserResponse;
import com.rental.rentalapplication.mapper.UserMapper;
import com.rental.rentalapplication.repository.UserRepository;
import com.rental.rentalapplication.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "User")
public class UserController {
    UserServiceImpl userServiceImpl;


    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResponseData(userServiceImpl.createUser(request));
        return apiResponse;
    }

    @GetMapping
    ApiResponse<Page<UserResponse>> getUsers(Integer pageNo, Integer pageSize) {
        Page<UserResponse> userResponses = userServiceImpl.getAllUsers(pageNo, pageSize);

        return ApiResponse.<Page<UserResponse>>builder()
                .message("Get users")
                .status(userResponses != null ? "success" : "failure")
                .timeStamp(LocalDateTime.now())
                .responseData(userResponses)
                .build();
    }


    @GetMapping("/user/username/{username}")
    ApiResponse<UserResponse> getUserByUsername(@PathVariable("username") String username) {
        UserResponse response = userServiceImpl.getUser(username);
        return ApiResponse.<UserResponse>builder()
                .message("Get user by id")
                .status(response != null ? "success" : "failure")
                .timeStamp(LocalDateTime.now())
                .responseData(response)
                .build();
    }

    @GetMapping("/user/id/{id}")
    ApiResponse<UserResponse> getUserByID(@PathVariable("id") String id) {
        UserResponse response = userServiceImpl.getUserById(id);
        return ApiResponse.<UserResponse>builder()
                .message("Get user by id")
                .status(response != null ? "success" : "failure")
                .timeStamp(LocalDateTime.now())
                .responseData(response)
                .build();
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        UserResponse userResponse = userServiceImpl.getMyInfo();
        return ApiResponse.<UserResponse>builder()
                .message("Get my info")
                .status(userResponse != null ? "success" : "failure")
                .timeStamp(LocalDateTime.now())
                .responseData(userResponse)
                .build();
    }

    @PutMapping("{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        UserResponse userResponse = userServiceImpl.updateUserByAdmin(userId, request);
        return ApiResponse.<UserResponse>builder()
                .message("Update user")
                .status(userResponse != null ? "success" : "failure")
                .timeStamp(LocalDateTime.now())
                .responseData(userResponse)
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable("userId") String userId) {
        userServiceImpl.deleteUser(userId);
        return ApiResponse.<String>builder().message("User has been deleted").build();
    }
}
