package com.example.demo.controller;

import com.example.demo.config.RequestIdFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class ApiController {
    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    private static final List<String> RANDOM_DETAILS = List.of(
            "priority-high",
            "priority-medium",
            "priority-low",
            "status-active",
            "status-pending",
            "region-east",
            "region-west"
    );

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health(HttpServletRequest request) {
        String requestId = requestId(request);
        log.info("Health endpoint requested");

        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "requestId", requestId,
                "timestamp", Instant.now().toString()
        ));
    }

    @GetMapping("/broker/{name}")
    public ResponseEntity<Map<String, Object>> brokerByName(
            @PathVariable String name,
            HttpServletRequest request) {

        String requestId = requestId(request);
        log.info("Broker endpoint requested for name={}", name);

        return ResponseEntity.ok(Map.of(
                "requestId", requestId,
                "name", name
        ));
    }

    @GetMapping("/broker/{name}/{details}")
    public ResponseEntity<Map<String, Object>> brokerWithDetails(
            @PathVariable String name,
            @PathVariable String details,
            HttpServletRequest request) {

        String randomDetail = RANDOM_DETAILS.get(
                ThreadLocalRandom.current().nextInt(RANDOM_DETAILS.size())
        );

        log.info("Broker details endpoint requested for name={} suppliedDetails={} randomDetails={}",
                name, details, randomDetail);

        return ResponseEntity.ok(Map.of(
                "requestId", requestId(request),
                "name", name,
                "suppliedDetails", details,
                "randomDetails", randomDetail
        ));
    }

    @GetMapping("/dac/{number}")
    public ResponseEntity<Map<String, Object>> dac(
            @PathVariable long number,
            HttpServletRequest request) {

        long upperBound = Math.max(2L, Math.abs(number) + 1L);
        long randomNumber = ThreadLocalRandom.current().nextLong(1L, upperBound);

        log.info("DAC endpoint requested for number={} generatedNumber={}", number, randomNumber);

        return ResponseEntity.ok(Map.of(
                "requestId", requestId(request),
                "inputNumber", number,
                "randomNumber", randomNumber
        ));
    }

    private String requestId(HttpServletRequest request) {
        Object value = request.getAttribute(RequestIdFilter.REQUEST_ID_ATTRIBUTE);
        return value == null ? "unknown" : value.toString();
    }
}
