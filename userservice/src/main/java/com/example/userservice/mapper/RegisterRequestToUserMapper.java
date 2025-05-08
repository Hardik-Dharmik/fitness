package com.example.userservice.mapper;

import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.model.User;

public class RegisterRequestToUserMapper {
    public static User mapToUser(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return user;
    }
}
