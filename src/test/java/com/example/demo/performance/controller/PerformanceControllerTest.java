package com.example.demo.performance.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.common.config.WebConfig;
import com.example.demo.common.dto.Cursor;
import com.example.demo.common.dto.CursorResult;
import com.example.demo.performance.dto.PerformanceDetailResponse;
import com.example.demo.performance.dto.PerformanceResponse;
import com.example.demo.performance.exception.PerformanceNotFoundException;
import com.example.demo.performance.service.PerformanceService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PerformanceController.class)
@Import(WebConfig.class)
class PerformanceControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private PerformanceService performanceService;

  @Test
  void getPerformance_usesDefaultCursorValues_whenCursorParamsOmitted() throws Exception {
    given(performanceService.getPerformance(any())).willReturn(CursorResult.of(List.of(), null));

    mockMvc.perform(get("/performances").param("memeberId", "1")).andExpect(status().isOk());

    verify(performanceService).getPerformance(new Cursor(Long.MAX_VALUE, 10));
  }

  @Test
  void getPerformance_usesProvidedCursorAndSize_whenParamsGiven() throws Exception {
    given(performanceService.getPerformance(any())).willReturn(CursorResult.of(List.of(), null));

    mockMvc
        .perform(
            get("/performances").param("memeberId", "1").param("cursor", "5").param("size", "3"))
        .andExpect(status().isOk());

    verify(performanceService).getPerformance(new Cursor(5L, 3));
  }

  @Test
  void getPerformance_returnsBody_withContentAndNextCursor() throws Exception {
    PerformanceResponse response =
        PerformanceResponse.builder()
            .id(1L)
            .title("공연1")
            .price(10000)
            .totalSeats(100)
            .thumbnailImage("thumb.jpg")
            .build();
    given(performanceService.getPerformance(any()))
        .willReturn(CursorResult.of(List.of(response), 1L));

    mockMvc
        .perform(get("/performances").param("memeberId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].id").value(1))
        .andExpect(jsonPath("$.content[0].title").value("공연1"))
        .andExpect(jsonPath("$.content[0].thumbnailImage").value("thumb.jpg"))
        .andExpect(jsonPath("$.nextCursor").value(1));
  }

  @Test
  void getPerformanceDetail_returnsOk_withDetailBody() throws Exception {
    LocalDateTime now = LocalDateTime.now();
    PerformanceDetailResponse detail =
        new PerformanceDetailResponse(
            "공연1", now, 10000, 100, now, now, "설명", List.of("img1.jpg", "img2.jpg"));
    given(performanceService.getPerformanceDetail(1L)).willReturn(detail);

    mockMvc
        .perform(get("/performances/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("공연1"))
        .andExpect(jsonPath("$.images[0]").value("img1.jpg"));
  }

  @Test
  void getPerformanceDetail_returns404Problem_whenPerformanceMissing() throws Exception {
    given(performanceService.getPerformanceDetail(999L))
        .willThrow(new PerformanceNotFoundException(999L));

    mockMvc
        .perform(get("/performances/999"))
        .andExpect(status().isNotFound())
        .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.detail").value("Performance not found: 999"));
  }

  @Test
  void createPerformance_returns201WithLocation_whenValid() throws Exception {
    given(performanceService.registerPerformance(any())).willReturn(42L);

    String body =
        "{\"title\":\"공연1\",\"performedAt\":\"2026-12-01T20:00:00\",\"price\":10000,"
            + "\"totalSeats\":100,\"bookingOpenAt\":\"2026-10-01T10:00:00\","
            + "\"bookingCloseAt\":\"2026-11-30T23:59:59\"}";

    mockMvc
        .perform(post("/performances").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/performances/42"));
  }

  @Test
  void createPerformance_returns400Problem_whenTitleBlank() throws Exception {
    String body =
        "{\"title\":\"\",\"performedAt\":\"2026-12-01T20:00:00\",\"price\":10000,"
            + "\"totalSeats\":100,\"bookingOpenAt\":\"2026-10-01T10:00:00\","
            + "\"bookingCloseAt\":\"2026-11-30T23:59:59\"}";

    mockMvc
        .perform(post("/performances").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith("application/problem+json"));

    verify(performanceService, never()).registerPerformance(any());
  }
}
