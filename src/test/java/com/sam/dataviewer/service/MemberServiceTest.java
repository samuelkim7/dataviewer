package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.dto.MemberDto;
import com.sam.dataviewer.dto.PasswordDto;
import com.sam.dataviewer.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원_가입() throws Exception {
        //given
        MemberDto dto = new MemberDto();
        dto.setUsername("kim");
        dto.setPassword("1234");

        //when
        Member member = memberService.join(dto);

        //then
        assertThat(member).isEqualTo(memberRepository.getOne(member.getId()));
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        //given
        MemberDto dto1 = new MemberDto();
        dto1.setUsername("kim");
        dto1.setPassword("1234");

        MemberDto dto2 = new MemberDto();
        dto2.setUsername("kim");
        dto2.setPassword("1234");

        //when
        memberService.join(dto1);

        //then
        assertThatThrownBy(() -> {
            memberService.join(dto2);
        }).isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("아이디 중복");
    }

    @Test
    public void 회원_정보_수정() throws Exception {
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
        memberService.updateMember(dto2);

        //then
        assertThat(dto2.getName())
                .isEqualTo(memberRepository.getOne(member.getId()).getName());
    }

    @Test
    public void 비밀번호_변경_기존_비밀번호_검증() throws Exception {
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
    public void 비밀번호_변경_비밀번호_재확인_검증() throws Exception {
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