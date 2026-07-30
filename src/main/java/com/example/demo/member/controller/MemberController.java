package com.example.demo.member.controller;

import com.example.demo.member.dto.MemberCreateRequest;
import com.example.demo.member.dto.MemberResponse;
import com.example.demo.member.dto.MemberUpdateRequest;
import com.example.demo.member.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @PostMapping
  public ResponseEntity<MemberResponse> create(@RequestBody MemberCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(memberService.create(request));
  }

  @GetMapping("/{id}")
  public MemberResponse getById(@PathVariable Long id) {
    return memberService.getById(id);
  }

  @GetMapping
  public List<MemberResponse> getAll() {
    return memberService.getAll();
  }

  @PutMapping("/{id}")
  public MemberResponse update(@PathVariable Long id, @RequestBody MemberUpdateRequest request) {
    return memberService.update(id, request);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    memberService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
