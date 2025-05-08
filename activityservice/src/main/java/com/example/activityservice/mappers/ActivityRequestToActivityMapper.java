package com.example.activityservice.mappers;

import com.example.activityservice.dto.ActivityRequest;
import com.example.activityservice.model.Activity;

public class ActivityRequestToActivityMapper {
    public static Activity map(ActivityRequest activityRequest) {
        return Activity.builder()
                .userId(activityRequest.getUserId())
                .type(activityRequest.getActivityType())
                .duration(activityRequest.getDuration())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .startTime(activityRequest.getStartTime())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .build();
    }
}
