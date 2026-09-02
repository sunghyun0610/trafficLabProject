package com.example.demo.performance.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.common.config.WebConfig;
import com.example.demo.common.dto.Cursor;
import com.example.demo.common.dto.CursorResult;
import com.example.demo.performance.dto.PerformanceResponse;
import com.example.demo.performance.service.PerformanceService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
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
}
