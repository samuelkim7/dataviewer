package com.sam.dataviewer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.dto.MemberDto;
import com.sam.dataviewer.dto.PasswordDto;
import com.sam.dataviewer.repository.MemberRepository;
import com.sam.dataviewer.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@WithMockUser(username = "kim", password = "1234", roles = "USER")
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("로그인 폼")
    @WithAnonymousUser
    public void loginFormTest() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @DisplayName("로그인")
    @WithAnonymousUser
    public void loginTest() throws Exception {
        getMember();
        mockMvc.perform(formLogin().user("kim").password("1234"))
                .andExpect(authenticated());
    }

    @Test
    @DisplayName("회원 가입 폼")
    @WithAnonymousUser
    public void createFormTest() throws Exception {
        mockMvc.perform(get("/member/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/createMemberForm"))
                .andExpect(model().attributeExists("memberDto"));
    }

    @Test
    @DisplayName("회원 가입")
    @WithAnonymousUser
    public void createMemberTest() throws Exception {
        MemberDto dto = new MemberDto(
                "kim", "1234", "John",
                "def@gmail.com", "01011112222",
                LocalDate.now(), null);
        mockMvc.perform(post("/member/new").with(csrf())
                .param("username", dto.getUsername())
                .param("password", dto.getPassword())
                .param("name", dto.getName())
                .param("email", dto.getEmail())
                .param("phoneNumber", dto.getPhoneNumber())
                .param("birthdate", dto.getBirthDate().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        Member member = memberRepository.findByUsername("kim");
        then(member.getName()).isEqualTo("John");
        then(member.getEmail()).isEqualTo("def@gmail.com");
    }

    @Test
    @DisplayName("회원 상세정보")
    public void memberDetailTest() throws Exception {
        Member member = getMember();
        MockHttpServletResponse response = mockMvc.perform(get("/member/memberDetail"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/memberDetail"))
                .andExpect(model().attributeExists("memberDto"))
                .andReturn().getResponse();

        then(response.getContentAsString())
                .contains("kim", "Sam", "abc@gmail.com");
    }

    @Test
    @DisplayName("회원 정보 수정")
    public void updateMemberTest() throws Exception {
        Member member = getMember();
        MemberDto dto = new MemberDto(
                member.getUsername(), member.getPassword(), "John",
                "def@gmail.com", "01011112222",
                LocalDate.now(), null);
        mockMvc.perform(post("/member/update").with(csrf())
                .param("username", dto.getUsername())
                .param("name", dto.getName())
                .param("email", dto.getEmail())
                .param("phoneNumber", dto.getPhoneNumber())
                .param("birthdate", dto.getBirthDate().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/member/memberDetail"));

        then(member.getName()).isEqualTo("John");
        then(member.getEmail()).isEqualTo("def@gmail.com");
        then(member.getPhoneNumber()).isEqualTo("01011112222");
    }

    @Test
    @DisplayName("비밀번호 수정 폼")
    public void updateFormTest() throws Exception {
        mockMvc.perform(get("/member/updatePassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/updatePasswordForm"))
                .andExpect(model().attributeExists("passwordDto"));
    }

    @Test
    @DisplayName("비밀번호 수정")
    public void updatePasswordTest() throws Exception {
        Member member = getMember();
        PasswordDto dto = new PasswordDto(
                "1234", "1111", "1111"
        );
        mockMvc.perform(post("/member/updatePassword").with(csrf())
                .param("currentPassword", dto.getCurrentPassword())
                .param("newPassword", dto.getNewPassword())
                .param("confirmPassword", dto.getConfirmPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        // 새 비밀번호로 로그인 테스트
        mockMvc.perform(formLogin().user("kim").password("1111"))
                .andExpect(authenticated());
    }

    private Member getMember() {
        MemberDto memberDto = new MemberDto();
        memberDto.setUsername("kim");
        memberDto.setPassword("1234");
        memberDto.setName("Sam");
        memberDto.setEmail("abc@gmail.com");
        return memberService.join(memberDto);
    }

}