package com.example.demo.performance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "performance")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Performance {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "performance_id")
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private LocalDateTime performedAt;

  @Column(nullable = false)
  private int price;// 가격은 모두 통일

  @Column(nullable = false)
  private int totalSeats;

  @Column(nullable = false)
  private LocalDateTime bookingOpenAt;//예약 오픈시점

  @Column(nullable = false)
  private LocalDateTime bookingCloseAt;//예약 마감시점


  private String description;
}
