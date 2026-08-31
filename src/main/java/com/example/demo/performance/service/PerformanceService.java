package com.example.demo.performance.service;

import com.example.demo.common.dto.Cursor;
import com.example.demo.common.dto.CursorResult;
import com.example.demo.performance.dto.PerformanceResponse;
import com.example.demo.performance.entity.Performance;
import com.example.demo.performance.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerformanceService {

  private final PerformanceRepository performanceRepository;

  public CursorResult<PerformanceResponse> getPerformance(Cursor cursor){
      List<Performance> performanceList = performanceRepository.findPerformanceByCursor(cursor.cursor(), cursor.size());

      Long nextCursor = performanceList.isEmpty() ? null : performanceList.get(performanceList.size()-1).getId(); // 마지막 공연 id
      List<PerformanceResponse> response = performanceList.stream().map(
              performance ->{
              return PerformanceResponse.from(performance);
              }
      ).toList();
      return CursorResult.of(response, nextCursor);
  }
}
