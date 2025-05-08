package com.example.userservice.services;

import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.mapper.RegisterRequestToUserMapper;
import com.example.userservice.mapper.UserToUserResponseMapper;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse register(@Valid RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User savedUser = userRepository.save(RegisterRequestToUserMapper.mapToUser(request));
        return UserToUserResponseMapper.toUserResponse(savedUser);
    }


    public UserResponse getUserProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserToUserResponseMapper.toUserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(UserToUserResponseMapper::toUserResponse).collect(Collectors.toList());
    }
}
