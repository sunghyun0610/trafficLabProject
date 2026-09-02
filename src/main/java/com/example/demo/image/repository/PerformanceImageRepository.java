package com.example.demo.image.repository;

import com.example.demo.image.entity.PerformanceImageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceImageRepository extends JpaRepository<PerformanceImageEntity, Long> {
  List<PerformanceImageEntity> findByPerformanceIdIn(List<Long> performanceIds);
  List<PerformanceImageEntity> findByPerformanceIdInAndThumbnailTrue(List<Long> performanceIds);
  List<PerformanceImageEntity> findByPerformanceId(Long performanceId);//상세조회
}
