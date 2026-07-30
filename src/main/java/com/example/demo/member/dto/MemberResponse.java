package com.example.demo.member.dto;

import com.example.demo.member.entity.Member;

public record MemberResponse(Long id, String name, String email) {

  public static MemberResponse from(Member member) {
    return new MemberResponse(member.getId(), member.getName(), member.getEmail());
  }
}
