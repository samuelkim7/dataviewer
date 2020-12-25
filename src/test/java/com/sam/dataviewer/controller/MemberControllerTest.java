package com.sam.dataviewer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam.dataviewer.dto.MemberDto;
import com.sam.dataviewer.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    @DisplayName("로그인")
    public void loginTest() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @DisplayName("회원가입폼")
    public void createFormTest() throws Exception {
        mockMvc.perform(get("/member/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/createMemberForm"))
                .andExpect(model().attributeExists("memberDto"));
    }

    /* 스프링 시큐리티 테스트를 적용해야 아래 두 메서드 테스트 가능한 것으로 보임 */
    @Test
    @DisplayName("회원 상세정보")
    @WithMockUser(roles = "USER")
    public void memberDetailTest() throws Exception {
        mockMvc.perform(get("/member/memberDetail"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/memberDetail"))
                .andExpect(model().attributeExists("memberDto"));
    }

    @Test
    @DisplayName("회원 정보 변경")
    public void updateMemberTest() throws Exception {
        MemberDto memberDto = getMemberDto();
        mockMvc.perform(post("/member/update").with(csrf())
                .content(objectMapper.writeValueAsString(memberDto)))
                .andExpect(status().isOk())
                .andExpect(view().name("member/memberDetail"));
//        verify(MemberService, times(1)).
    }

    private MemberDto getMemberDto() {
        MemberDto memberDto = new MemberDto(
                "sam", "1234", "kim",
                "abc@gmail.com", "1234",
                LocalDate.now(), null);
        return memberDto;
    }

}