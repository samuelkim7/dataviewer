package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /*
    * 회원 가입
    */
    @Transactional
    public Long join(Member member) {
        validateUserId(member);
        memberRepository.save(member);
        return member.getId();
    }

    /*
    * 아이디 중복 검증
    */
    private void validateUserId(Member member) {
        Member existingMember = memberRepository.findByUserId(member.getUserId());
        if (existingMember != null) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }
}
