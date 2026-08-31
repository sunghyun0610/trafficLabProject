package com.example.demo.performance.dto;

import com.example.demo.performance.entity.Performance;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PerformanceResponse(
    Long id,
    String title,
    LocalDateTime performedAt,
    int price,
    int totalSeats,
    LocalDateTime bookingOpenAt,
    LocalDateTime bookingCloseAt,
    String thumbnailImage) {

    public static PerformanceResponse from(Performance performance){
        return new PerformanceResponse(
                performance.getId(),
                performance.getTitle(),
                performance.getPerformedAt(),
                performance.getPrice(),
                performance.getTotalSeats(),
                performance.getBookingOpenAt(),
                performance.getBookingCloseAt(),
                performance.getImage()
        );
    }
}


//공연 일반 조회