package com.ShareText.demo.controllers;

import com.ShareText.demo.dto.ApiResponse;
import com.ShareText.demo.dto.RegisterUserRequest;
import com.ShareText.demo.dto.UserLoginRequest;
import com.ShareText.demo.dto.UserResponse;
import com.ShareText.demo.models.User;
import com.ShareText.demo.services.AuthService.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserRequest request) {

        String message = authService.register(request);

        ApiResponse<String> response =
                new ApiResponse<>(201, message, "User registered successfully");

        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest request) {

        User user = this.authService.login(request);

        ApiResponse<UserResponse> response =
                new ApiResponse<>(200, new UserResponse(user), "Login successful");

        return ResponseEntity.ok(response);
    }
}
