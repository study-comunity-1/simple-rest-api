package com.study.bookstore.domain.member.service.read;

import com.study.bookstore.domain.member.entity.Member;
import com.study.bookstore.domain.member.entity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadMemberService {

  private final MemberRepository memberRepository;

  public Member findMemberByEmail(String email) {
    return memberRepository.findMemberByEmail(email);
  }
}
