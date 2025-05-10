package com.example.activityservice.service;

import com.example.activityservice.model.Activity;
import com.example.activityservice.repository.ActivityRepository;
import com.example.activityservice.dto.ActivityRequest;
import com.example.activityservice.dto.ActivityResponse;
import com.example.activityservice.mappers.ActivityRequestToActivityMapper;
import com.example.activityservice.mappers.ActivityToActivityResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ActivityResponse trackActivity(ActivityRequest activityRequest) {
        if(!userValidationService.validateUser(activityRequest.getUserId())) {
            throw new RuntimeException("Invalid user");
        }

        Activity savedActivity = activityRepository.save(ActivityRequestToActivityMapper.map(activityRequest));

        try{
            rabbitTemplate.convertAndSend(exchange, routingKey, savedActivity);
        } catch(Exception e){
            log.error("Failed to publish activity to queue : {}",e.getMessage());
        }

        return ActivityToActivityResponseMapper.map(savedActivity);
    }

    public List<ActivityResponse> getUserActivities(String userId) {
        return activityRepository.findByUserId(userId).stream()
                .map(ActivityToActivityResponseMapper::map)
                .toList();
    }

    public ActivityResponse getActivityById(String activityId) {
        return activityRepository.findById(activityId)
                .map(ActivityToActivityResponseMapper::map)
                .orElseThrow(() -> new RuntimeException("Activity not found with id: " + activityId));
    }
}
