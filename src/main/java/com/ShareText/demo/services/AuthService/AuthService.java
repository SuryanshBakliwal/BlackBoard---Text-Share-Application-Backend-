package com.ShareText.demo.services.AuthService;

import com.ShareText.demo.dto.RegisterUserRequest;
import com.ShareText.demo.dto.UserLoginRequest;
import com.ShareText.demo.models.User;

public interface AuthService {
    String register(RegisterUserRequest request);
    User login(UserLoginRequest request);
}
