package com.example.demo.performance.repository;

import com.example.demo.performance.dto.PerformanceCreateRequest;
import com.example.demo.performance.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerformanceRepository
    extends JpaRepository<Performance, Long>, PerformanceRepositoryCustom {

    Optional<Performance> findPerformanceById(Long performanceId);

}
