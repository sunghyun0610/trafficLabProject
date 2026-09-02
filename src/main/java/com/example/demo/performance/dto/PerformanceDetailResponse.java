package com.example.demo.performance.dto;

import com.example.demo.performance.entity.Performance;

import java.time.LocalDateTime;//공연 상세조회 dto
import java.util.List;

public record PerformanceDetailResponse(
        String title,
        LocalDateTime performedAt,
        int price,
        int totalSeats,
        LocalDateTime bookingOpenAt,
        LocalDateTime bookingCloseAt,
        String description,
        List<String> images
) {
    public static PerformanceDetailResponse from(Performance performance, List<String> images){
        return new PerformanceDetailResponse(
                performance.getTitle(),
                performance.getPerformedAt(),
                performance.getPrice(),
                performance.getTotalSeats(),
                performance.getBookingOpenAt(),
                performance.getBookingCloseAt(),
                performance.getDescription(),
                images
        );
    }
}

