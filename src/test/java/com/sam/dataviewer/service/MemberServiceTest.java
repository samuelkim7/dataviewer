package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.dto.MemberDto;
import com.sam.dataviewer.dto.PasswordDto;
import com.sam.dataviewer.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;
    @Mock
    MemberRepository memberRepository;
    @Captor
    private ArgumentCaptor<Member> argumentCaptor;
    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원 가입")
    public void joinTest() throws Exception {
        //given
        MemberDto dto = new MemberDto();
        dto.setUsername("kim");
        dto.setPassword("1234");
        given(passwordEncoder.encode(dto.getPassword())).willReturn(dto.getPassword());

        //when
        Member member = memberService.join(dto);

        //then
        verify(memberRepository, times(1)).save(argumentCaptor.capture());
        then(argumentCaptor.getValue()).isNotNull();
        then(argumentCaptor.getValue().getUsername()).isEqualTo("kim");
        then(argumentCaptor.getValue().getPassword()).isEqualTo("1234");
    }

    @Test
    @DisplayName("아이디 중복 검증")
    public void validateUsernameTest() {
        //given
        MemberDto dto = new MemberDto();
        dto.setUsername("kim");
        dto.setPassword("1234");
        given(passwordEncoder.encode(dto.getPassword())).willReturn(dto.getPassword());

        //when
        Member member = memberService.join(dto);
        given(memberRepository.findByUsername(dto.getUsername())).willReturn(member);

        //then
        assertThatThrownBy(() -> {
            memberService.validateUsername(member.getUsername());
        }).isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("아이디 중복");
    }

    @Test
    @DisplayName("회원 정보 수정")
    public void updateMemberTest() throws Exception {
        //given
        MemberDto dto1 = new MemberDto();
        dto1.setUsername("kim");
        dto1.setPassword("1234");
        dto1.setName("김치");
        Member member = memberService.join(dto1);

        MemberDto dto2 = new MemberDto();
        dto2.setUsername("kim");
        dto2.setPassword("1234");
        dto2.setName("단무지");

        //when
        given(memberRepository.findByUsername(any())).willReturn(member);
        memberService.updateMember(dto2);

        //then
        verify(memberRepository, times(1)).(argumentCaptor.capture());
        then(argumentCaptor.getValue()).isNotNull();
        then(argumentCaptor.getValue().getUsername()).isEqualTo("kim");
        then(argumentCaptor.getValue().getPassword()).isEqualTo("1234");
        assertThat(dto2.getName())
                .isEqualTo(memberRepository.getOne(member.getId()).getName());
    }

    @Test
    @DisplayName("기존 비밀번호 검증")
    public void validateCurrentPasswordTest() throws Exception {
        //given
        MemberDto memberDto = new MemberDto();
        memberDto.setUsername("kim");
        memberDto.setPassword("1234");

        PasswordDto passwordDto = new PasswordDto();
        passwordDto.setCurrentPassword("1111");

        //when
        Member member = memberService.join(memberDto);

        //then
        assertThatThrownBy(() -> {
            memberService.updatePassword(member.getUsername(), passwordDto);
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("비밀번호 틀림");
    }

    @Test
    @DisplayName("비밀번호 재확인 검증")
    public void validateConfirmPasswordTest() throws Exception {
        //given
        MemberDto memberDto = new MemberDto();
        memberDto.setUsername("kim");
        memberDto.setPassword("1234");

        PasswordDto passwordDto = new PasswordDto();
        passwordDto.setCurrentPassword("1234");
        passwordDto.setNewPassword("1111");
        passwordDto.setConfirmPassword("2222");

        //when
        Member member = memberService.join(memberDto);

        //then
        assertThatThrownBy(() -> {
            memberService.updatePassword(member.getUsername(), passwordDto);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호 재확인 실패");
    }
}