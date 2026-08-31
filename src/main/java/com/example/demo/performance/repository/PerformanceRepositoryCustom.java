package com.example.demo.performance.repository;

import com.example.demo.performance.entity.Performance;
import java.util.List;

public interface PerformanceRepositoryCustom {

  List<Performance> findPerformanceByCursor(Long cursor, Integer size);
}
