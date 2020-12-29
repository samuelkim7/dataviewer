package com.sam.dataviewer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam.dataviewer.domain.*;
import com.sam.dataviewer.dto.EstimateDto;
import com.sam.dataviewer.dto.MemberDto;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.repository.EstimateRepository;
import com.sam.dataviewer.service.EstimateService;
import com.sam.dataviewer.service.MemberService;
import com.sam.dataviewer.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
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
class EstimateControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private EstimateService estimateService;
    @Autowired
    private EstimateRepository estimateRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("견적 리스트 보기")
    public void estimateListTest() throws Exception {
        Long orderId = getOrder();
        getEstimate(orderId, "견적1");
        getEstimate(orderId, "견적2");
        MockHttpServletResponse response = mockMvc.perform(get("/estimates"))
                .andExpect(status().isOk())
                .andExpect(view().name("estimate/estimateList"))
                .andExpect(model().attributeExists("estimateDtos"))
                .andReturn().getResponse();

        then(response.getContentAsString())
                .contains("견적1", "견적2");
    }

    @Test
    @DisplayName("견적 상세보기")
    public void estimateDetailTest() throws Exception {
        Long orderId = getOrder();
        Estimate estimate = getEstimate(orderId, "견적");
        MockHttpServletResponse response = mockMvc.perform(
                get("/estimate/estimateDetail/{id}", estimate.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("estimate/estimateDetail"))
                .andExpect(model().attributeExists("orderDto", "estimateDto"))
                .andReturn().getResponse();

        then(response.getContentAsString())
                .contains("의뢰", "견적", "내용", "100000");
    }

    @Test
    @DisplayName("견적 승낙하기")
    public void acceptEstimateTest() throws Exception {
        Long orderId = getOrder();
        Estimate estimate = getEstimate(orderId, "견적");
        Order order = estimate.getOrder();
        mockMvc.perform(post("/estimate/accept").with(csrf())
                .param("orderDtoId", order.getId().toString())
                .param("id", estimate.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/estimates"));

        then(order.getStatus()).isEqualTo(OrderStatus.ORDER);
        then(estimate.getStatus()).isEqualTo(EstimateStatus.ACCEPT);
    }

    private Long getOrder() {
        MemberDto memberDto = new MemberDto(
                "kim", "1234", "Sam", "abc@gmail.com",
                "01011110000", null, null
        );
        Member member = memberService.join(memberDto);

        OrderDto orderDto = new OrderDto(
                null, "의뢰", "내용",
                null, null
        );
        return orderService.order(member.getUsername(), orderDto);
    }

    private Estimate getEstimate(Long orderId, String title) {
        EstimateDto estimateDto = new EstimateDto(
                null, title, "내용", 100000L,
                null, null, null
        );
        Long id = estimateService.request(orderId, estimateDto);
        return estimateRepository.getOne(id);
    }

}