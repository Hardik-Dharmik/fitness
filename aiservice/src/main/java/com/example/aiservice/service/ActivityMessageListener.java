package com.example.aiservice.service;

import com.example.aiservice.model.Activity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    @RabbitListener(queues = "activity.queue")
    public void processActivity(byte[] messageBytes) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            Activity activity = mapper.readValue(new String(messageBytes, StandardCharsets.UTF_8), Activity.class);
            log.info("Received activity: {}", activity.getId());
        } catch (IOException e) {
            log.error("Failed to parse activity message", e);
        }

    }
}
