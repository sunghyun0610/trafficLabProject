package com.example.demo.performance.exception;

public class PerformanceNotFoundException extends RuntimeException {

  public PerformanceNotFoundException(Long performanceId) {
    super("Performance not found: " + performanceId);
  }
}
