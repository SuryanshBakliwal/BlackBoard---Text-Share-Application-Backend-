package com.ShareText.demo.dto;

import com.ShareText.demo.models.User;

public class UserResponse {
    private String username;
    private String email;

    // constructor
    public UserResponse(User user) {
        this.username = user.getName();
        this.email = user.getEmail();
    }
}
