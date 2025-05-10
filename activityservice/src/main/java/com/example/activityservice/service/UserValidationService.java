package com.example.activityservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class UserValidationService {
    @Autowired
    private final WebClient.Builder userServiceWebClient;

    public boolean validateUser(String userId) {
        try {
            String url = "http://USERSERVICE/api/users/validate/" + userId;
            return Boolean.TRUE.equals(userServiceWebClient.build().get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block());
        } catch (WebClientResponseException e) {
            if(e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new RuntimeException("User not found :" + userId);
            if(e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                throw new RuntimeException("Internal server error :" + e.getMessage());
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST)
                throw new RuntimeException("Bad request :" + e.getMessage());
            return false;
        }
    }
}
