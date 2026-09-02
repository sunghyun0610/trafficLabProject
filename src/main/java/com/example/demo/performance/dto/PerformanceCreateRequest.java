package com.example.demo.performance.dto;

import com.example.demo.performance.entity.Performance;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PerformanceCreateRequest(
    @NotBlank String title,
    @NotNull LocalDateTime performedAt,
    @Positive int price,
    @Positive int totalSeats,
    @NotNull LocalDateTime bookingOpenAt,
    @NotNull LocalDateTime bookingCloseAt) {

  @AssertTrue(message = "예약 기간이 올바르지 않습니다 (오픈 < 마감 <= 공연일시)")
  private boolean isValidSchedule() {
    if (bookingOpenAt == null || bookingCloseAt == null || performedAt == null) {
      return true;
    }
    return bookingOpenAt.isBefore(bookingCloseAt) && !bookingCloseAt.isAfter(performedAt);
  }

  public Performance toEntity() {
    return Performance.builder()
        .title(title)
        .performedAt(performedAt)
        .price(price)
        .totalSeats(totalSeats)
        .bookingOpenAt(bookingOpenAt)
        .bookingCloseAt(bookingCloseAt)
        .build();
  }
}
