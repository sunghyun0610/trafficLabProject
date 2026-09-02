package com.example.demo.performance.service;

import com.example.demo.common.dto.Cursor;
import com.example.demo.common.dto.CursorResult;
import com.example.demo.image.entity.PerformanceImageEntity;
import com.example.demo.image.repository.PerformanceImageRepository;
import com.example.demo.performance.dto.PerformanceDetailResponse;
import com.example.demo.performance.dto.PerformanceResponse;
import com.example.demo.performance.entity.Performance;
import com.example.demo.performance.repository.PerformanceRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerformanceService {

  private final PerformanceRepository performanceRepository;
  private final PerformanceImageRepository performanceImageRepository;

  public CursorResult<PerformanceResponse> getPerformance(Cursor cursor) {
    List<Performance> performanceList =
        performanceRepository.findPerformanceByCursor(cursor.cursor(), cursor.size());

    Long nextCursor =
        performanceList.isEmpty()
            ? null
            : performanceList.get(performanceList.size() - 1).getId(); // 마지막 공연 id

    Map<Long, String> thumbnailByPerformanceId = findThumbnails(performanceList);

    List<PerformanceResponse> response =
        performanceList.stream()
            .map(
                performance -> {
                  return PerformanceResponse.from(
                      performance, thumbnailByPerformanceId.get(performance.getId()));
                })
            .toList();
    return CursorResult.of(response, nextCursor);
  }

  public PerformanceDetailResponse getPerformanceDetail(Long performanceId) {
    // performance랑 이미지 다 가져와야함
    Performance performance = performanceRepository.findPerformanceById(performanceId);
    // 이미지도 가져와야지
    List<PerformanceImageEntity> imageList =
        performanceImageRepository.findByPerformanceId(performanceId);
    List<String> imageUrlList = imageList.stream().map(image -> image.getImageUrl()).toList();
    return PerformanceDetailResponse.from(performance, imageUrlList);
  }

  private Map<Long, String> findThumbnails(List<Performance> performanceList) {
    if (performanceList.isEmpty()) return Map.of();
    else {
      List<Long> performanceIds = performanceList.stream().map(Performance::getId).toList();
      return performanceImageRepository
          .findByPerformanceIdInAndThumbnailTrue(performanceIds)
          .stream()
          .collect(
              Collectors.toMap(
                  image -> image.getPerformance().getId(),
                  image -> image.getImageUrl(),
                  (first, second) -> first));
    }
  }
}
