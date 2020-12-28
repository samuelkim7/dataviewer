package com.sam.dataviewer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.dto.MemberDto;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.repository.MemberRepository;
import com.sam.dataviewer.repository.OrderRepository;
import com.sam.dataviewer.service.MemberService;
import com.sam.dataviewer.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@WithMockUser(username = "kim", password = "1234", roles = "USER")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("의뢰 신청 폼")
    public void createFormTest() throws Exception {
        getMember();
        mockMvc.perform(get("/order/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/createOrderForm"))
                .andExpect(model().attributeExists("orderDto"));
    }

    @Test
    @DisplayName("의뢰 신청")
    public void createOrderTest() throws Exception {
        getMember();
        OrderDto dto = new OrderDto(
                null, "의뢰", "내용",
                null, null
        );
        mockMvc.perform(post("/order/new").with(csrf())
                .param("title", dto.getTitle())
                .param("content", dto.getContent()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));

        Order order = orderRepository.findByTitle(dto.getTitle());
        then(order.getTitle()).isEqualTo("의뢰");
        then(order.getContent()).isEqualTo("내용");
    }

    @Test
    @DisplayName("의뢰 상세보기")
    public void orderDetailTest() throws Exception {
        Member member = getMember();
        Order order = getOrder(member);
        MockHttpServletResponse response = mockMvc.perform(
                get("/order/orderDetail/{id}", order.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("order/orderDetail"))
                .andExpect(model().attributeExists("orderDto", "fileDtos"))
                .andReturn().getResponse();

        then(response.getContentAsString())
                .contains("의뢰", "내용");
    }

    @Test
    @DisplayName("의뢰 수정하기")
    public void updateOrderTest() throws Exception {
        Member member = getMember();
        Order order = getOrder(member);
        OrderDto dto = new OrderDto(
                order.getId(), "의뢰2", "내용2",
                null, null
        );
        mockMvc.perform(post("/order/update").with(csrf())
                .param("id", dto.getId().toString())
                .param("title", dto.getTitle())
                .param("content", dto.getContent()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));

        then(order.getTitle()).isEqualTo("의뢰2");
        then(order.getContent()).isEqualTo("내용2");
    }

    private Member getMember() {
        MemberDto memberDto = new MemberDto(
                "kim", "1234", "Sam", "abc@gmail.com",
                "01011110000", null, null
        );
        return memberService.join(memberDto);
    }

    private Order getOrder(Member member) {
        OrderDto orderDto = new OrderDto(
                null, "의뢰", "내용",
                null, null
        );
        Long id = orderService.order(member.getUsername(), orderDto);
        return orderRepository.getOne(id);
    }

    private MockMultipartFile getMultipartFile(String originalFileName) {
        return new MockMultipartFile(
                "data", originalFileName,
                "text/plain", originalFileName.getBytes());
    }

}