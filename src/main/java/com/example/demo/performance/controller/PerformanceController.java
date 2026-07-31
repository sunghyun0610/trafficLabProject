package com.example.demo.performance.controller;

import com.example.demo.performance.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/performances")
@RequiredArgsConstructor
public class PerformanceController {

  private final PerformanceService performanceService;
}
