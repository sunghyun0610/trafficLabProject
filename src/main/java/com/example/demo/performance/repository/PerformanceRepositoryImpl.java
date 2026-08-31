package com.example.demo.performance.repository;

import static com.example.demo.performance.entity.QPerformance.performance;

import com.example.demo.performance.entity.Performance;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PerformanceRepositoryImpl implements PerformanceRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Performance> findPerformanceByCursor(Long cursor, Integer size) {
    return queryFactory
        .selectFrom(performance)
        .where(performance.id.lt(cursor))
        .orderBy(performance.id.desc())
        .limit(size)
        .fetch();
  }
}
