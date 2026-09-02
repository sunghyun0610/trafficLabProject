package com.example.demo.performance.repository;

import com.example.demo.performance.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceRepository
    extends JpaRepository<Performance, Long>, PerformanceRepositoryCustom {

    Performance findPerformanceById(Long performanceId);
}
