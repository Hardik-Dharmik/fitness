package com.example.activityservice.service;

import com.example.activityservice.ActivityRepository;
import com.example.activityservice.dto.ActivityRequest;
import com.example.activityservice.dto.ActivityResponse;
import com.example.activityservice.mappers.ActivityRequestToActivityMapper;
import com.example.activityservice.mappers.ActivityToActivityResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityResponse trackActivity(ActivityRequest activityRequest) {
        return ActivityToActivityResponseMapper.map(
                activityRepository.save(
                        ActivityRequestToActivityMapper.map(activityRequest)
                )
        );
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
