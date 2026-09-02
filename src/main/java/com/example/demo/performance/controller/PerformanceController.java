package com.example.demo.performance.controller;

import com.example.demo.common.annotation.CursorDefault;
import com.example.demo.common.dto.Cursor;
import com.example.demo.common.dto.CursorResult;
import com.example.demo.performance.dto.PerformanceCreateRequest;
import com.example.demo.performance.dto.PerformanceDetailResponse;
import com.example.demo.performance.dto.PerformanceResponse;
import com.example.demo.performance.service.PerformanceService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/performances")
@RequiredArgsConstructor
public class PerformanceController {

  private final PerformanceService performanceService;

  @GetMapping
  public CursorResult<PerformanceResponse> getPerformance(@CursorDefault Cursor cursor) {
    CursorResult<PerformanceResponse> performanceList = performanceService.getPerformance(cursor);
    return performanceList;
  }

  @GetMapping("/{performanceId}")
  public PerformanceDetailResponse getPerformanceDetail(@PathVariable Long performanceId) {
    return performanceService.getPerformanceDetail(performanceId);
  }

  @PostMapping
  public ResponseEntity<Void> createPerformance(
      @Valid @RequestBody PerformanceCreateRequest request) {
    Long id = performanceService.registerPerformance(request);
    return ResponseEntity.created(URI.create("/performances/" + id)).build();
  }
}
