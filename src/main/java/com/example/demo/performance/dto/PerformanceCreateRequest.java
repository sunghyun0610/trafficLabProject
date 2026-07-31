package com.example.demo.performance.dto;

import java.time.LocalDateTime;

public record PerformanceCreateRequest(
    String title,
    String venue,
    LocalDateTime performedAt,
    int price,
    int totalSeats,
    LocalDateTime bookingOpenAt,
    LocalDateTime bookingCloseAt) {}
