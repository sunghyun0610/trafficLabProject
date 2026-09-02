package com.example.demo.performance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;

import com.example.demo.common.dto.Cursor;
import com.example.demo.common.dto.CursorResult;
import com.example.demo.image.entity.PerformanceImageEntity;
import com.example.demo.image.repository.PerformanceImageRepository;
import com.example.demo.performance.dto.PerformanceResponse;
import com.example.demo.performance.entity.Performance;
import com.example.demo.performance.repository.PerformanceRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PerformanceServiceTest {

  @Mock private PerformanceRepository performanceRepository;
  @Mock private PerformanceImageRepository performanceImageRepository;
  @InjectMocks private PerformanceService performanceService;

  @Test
  void getPerformance_returnsResponses_withThumbnailForSomeAndNullForOthers() {
    Performance performance1 = createPerformance(1L, "공연1");
    Performance performance2 = createPerformance(2L, "공연2");
    Cursor cursor = new Cursor(Long.MAX_VALUE, 10);

    given(performanceRepository.findPerformanceByCursor(cursor.cursor(), cursor.size()))
        .willReturn(List.of(performance1, performance2));

    PerformanceImageEntity thumbnail = new PerformanceImageEntity(performance1, "thumb1.jpg", true);
    given(performanceImageRepository.findByPerformanceIdInAndThumbnailTrue(List.of(1L, 2L)))
        .willReturn(List.of(thumbnail));

    CursorResult<PerformanceResponse> result = performanceService.getPerformance(cursor);

    assertThat(result.content()).hasSize(2);
    assertThat(result.content().get(0).thumbnailImage()).isEqualTo("thumb1.jpg");
    assertThat(result.content().get(1).thumbnailImage()).isNull();
    assertThat(result.nextCursor()).isEqualTo(2L);
  }

  @Test
  void getPerformance_returnsEmptyResult_whenRepositoryReturnsEmptyList() {
    Cursor cursor = new Cursor(Long.MAX_VALUE, 10);
    given(performanceRepository.findPerformanceByCursor(cursor.cursor(), cursor.size()))
        .willReturn(List.of());

    CursorResult<PerformanceResponse> result = performanceService.getPerformance(cursor);

    assertThat(result.content()).isEmpty();
    assertThat(result.nextCursor()).isNull();
    verifyNoInteractions(performanceImageRepository);
  }

  private Performance createPerformance(Long id, String title) {
    Performance performance = BeanUtils.instantiateClass(Performance.class);
    ReflectionTestUtils.setField(performance, "id", id);
    ReflectionTestUtils.setField(performance, "title", title);
    ReflectionTestUtils.setField(performance, "performedAt", LocalDateTime.now());
    ReflectionTestUtils.setField(performance, "price", 10000);
    ReflectionTestUtils.setField(performance, "totalSeats", 100);
    ReflectionTestUtils.setField(performance, "bookingOpenAt", LocalDateTime.now().minusDays(1));
    ReflectionTestUtils.setField(performance, "bookingCloseAt", LocalDateTime.now().plusDays(1));
    return performance;
  }
}
