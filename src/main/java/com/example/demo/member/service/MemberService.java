package com.example.demo.member.service;

import com.example.demo.member.dto.MemberCreateRequest;
import com.example.demo.member.dto.MemberResponse;
import com.example.demo.member.dto.MemberUpdateRequest;
import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

  private final MemberRepository memberRepository;

  @Transactional
  public MemberResponse create(MemberCreateRequest request) {
    Member member =
        Member.builder()
            .name(request.name())
            .email(request.email())
            .password(request.password())
            .build();
    return MemberResponse.from(memberRepository.save(member));
  }

  public MemberResponse getById(Long id) {
    return MemberResponse.from(findMember(id));
  }

  public List<MemberResponse> getAll() {
    return memberRepository.findAll().stream().map(MemberResponse::from).toList();
  }

  @Transactional
  public MemberResponse update(Long id, MemberUpdateRequest request) {
    Member member = findMember(id);
    member.update(request.name(), request.email(), request.password());
    return MemberResponse.from(member);
  }

  @Transactional
  public void delete(Long id) {
    memberRepository.deleteById(id);
  }

  private Member findMember(Long id) {
    return memberRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Member not found: " + id));
  }
}
