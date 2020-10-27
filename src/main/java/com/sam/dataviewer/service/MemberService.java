package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.dto.MemberDto;
import com.sam.dataviewer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /* 회원 가입 */
    @Transactional
    public Long join(MemberDto Dto) {
        //회원 아이디 중복 확인
        validateUsername(Dto.getUsername());

        //password 암호화
        String encodedPassword = passwordEncoder.encode(Dto.getPassword());

        Member member = Member.createMember(
                Dto.getUsername(), encodedPassword,
                Dto.getName(), Dto.getEmail(),
                Dto.getPhoneNumber(), Dto.getBirthDate(),
                Dto.getAddress()
        );
        memberRepository.save(member);
        return member.getId();
    }

    /* 아이디 중복 검증 */
    private void validateUsername(String username) {
        Member existingMember = memberRepository.findByUsername(username);
        if (existingMember != null) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    /* 회원 전체 조회 */
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    /* 회원 한 명 조회 */
    public Member findOne(Long memberId){
        return memberRepository.getOne(memberId);
    }
}
