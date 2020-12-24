package com.sam.dataviewer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam.dataviewer.dto.MemberDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
    public void memberDetailTest() throws Exception {
        mockMvc.perform(get("/member/memberDetail"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/memberDetail"))
                .andExpect(model().attributeExists("memberDto"));
    }

    @Test
    @DisplayName("회원 정보 변경")
    public void updateMemberTest() throws Exception {
        MemberDto memberDto = new MemberDto(
                "sam", "1234", "kim",
                "abc@gmail.com", "1234",
                LocalDate.now(), null);
        mockMvc.perform(post("/member/update")
                .content(objectMapper.writeValueAsString(memberDto)))
                .andExpect(status().isOk())
                .andExpect(view().name("member/memberDetail"));
    }

}