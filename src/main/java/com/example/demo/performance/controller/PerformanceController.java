package com.example.demo.performance.controller;

import com.example.demo.common.annotation.CursorDefault;
import com.example.demo.common.dto.Cursor;
import com.example.demo.common.dto.CursorResult;
import com.example.demo.performance.dto.PerformanceResponse;
import com.example.demo.performance.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/performances")
@RequiredArgsConstructor
public class PerformanceController {

  private final PerformanceService performanceService;

  @GetMapping
    public CursorResult<PerformanceResponse> getPerformance(@CursorDefault Cursor cursor, long memeberId){
      CursorResult<PerformanceResponse> performanceList = performanceService.getPerformance(cursor);
      return performanceList;

  }
}
