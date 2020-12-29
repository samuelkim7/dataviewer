package com.sam.dataviewer.adminController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam.dataviewer.domain.*;
import com.sam.dataviewer.dto.EstimateDto;
import com.sam.dataviewer.dto.MemberDto;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.repository.EstimateRepository;
import com.sam.dataviewer.repository.OrderRepository;
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

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@WithMockUser(roles = "ADMIN")
class AdminEstimateControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private EstimateService estimateService;
    @Autowired
    private EstimateRepository estimateRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;


    @Test
    @DisplayName("견적 신청 폼")
    public void createFormTest() throws Exception {
        Member member = getMember();
        getOrder(member, "의뢰1");
        getOrder(member, "의뢰2");
        MockHttpServletResponse response =
                mockMvc.perform(get("/admin/estimate/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/estimate/createEstimateForm"))
                .andExpect(model().attributeExists("orders", "estimateDto"))
                .andReturn().getResponse();

        then(response.getContentAsString()).contains("의뢰1", "의뢰2");
    }

    @Test
    @DisplayName("견적 신청")
    public void createEstimateTest() throws Exception {
        Member member = getMember();
        Order order = getOrder(member, "의뢰");
        EstimateDto dto = new EstimateDto(
                null, "견적", "내용", 100000L,
                "10일", null, null
        );
        mockMvc.perform(post("/admin/estimate/new").with(csrf())
                .param("orderId", order.getId().toString())
                .param("title", dto.getTitle())
                .param("content", dto.getContent())
                .param("price", dto.getPrice().toString())
                .param("duration", dto.getDuration()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/estimates"));

        List<Estimate> estimates = order.getEstimates();
        then(estimates.get(0).getTitle()).isEqualTo("견적");
        then(estimates.get(0).getContent()).isEqualTo("내용");
        then(estimates.get(0).getPrice()).isEqualTo(100000L);
        then(estimates.get(0).getDuration()).isEqualTo("10일");
        then(estimates.get(0).getOrder()).isEqualTo(order);
    }

    @Test
    @DisplayName("견적 리스트 보기")
    public void estimateListTest() throws Exception {
        Member member = getMember();
        Order order = getOrder(member, "의뢰");
        Estimate estimate1 = getEstimate(order.getId(), "견적1");
        Estimate estimate2 = getEstimate(order.getId(), "견적2");
        MockHttpServletResponse response =
                mockMvc.perform(get("/admin/estimates"))
                        .andExpect(status().isOk())
                        .andExpect(view().name("admin/estimate/estimateList"))
                        .andExpect(model().attributeExists("estimateDtos"))
                        .andReturn().getResponse();

        then(response.getContentAsString()).contains("견적1", "견적2");
    }

    @Test
    @DisplayName("견적 상세보기")
    public void estimateDetailTest() throws Exception {
        Member member = getMember();
        Order order = getOrder(member, "의뢰");
        Estimate estimate = getEstimate(order.getId(), "견적");
        MockHttpServletResponse response = mockMvc.perform(
                get("/admin/estimate/estimateDetail/{id}", estimate.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/estimate/estimateDetail"))
                .andExpect(model().attributeExists("orderDto", "estimateDto"))
                .andReturn().getResponse();

        then(response.getContentAsString())
                .contains("의뢰", "견적", "내용", "100000");
    }

    @Test
    @DisplayName("의뢰 수정하기")
    public void updateOrderTest() throws Exception {
        Member member = getMember();
        Order order = getOrder(member, "의뢰");
        Estimate estimate = getEstimate(order.getId(), "견적");
        EstimateDto dto = new EstimateDto(
                estimate.getId(), "견적1", "내용1",
                200000L, "20일", null, null
        );
        mockMvc.perform(post("/admin/estimate/update").with(csrf())
                .param("id", dto.getId().toString())
                .param("title", dto.getTitle())
                .param("content", dto.getContent())
                .param("price", dto.getPrice().toString())
                .param("duration", dto.getDuration()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/estimates"));

        then(estimate.getTitle()).isEqualTo("견적1");
        then(estimate.getContent()).isEqualTo("내용1");
        then(estimate.getPrice()).isEqualTo(200000L);
        then(estimate.getDuration()).isEqualTo("20일");
    }

    @Test
    @DisplayName("견적 취소하기")
    public void cancelEstimateTest() throws Exception {
        Member member = getMember();
        Order order = getOrder(member, "의뢰");
        Estimate estimate = getEstimate(order.getId(), "견적");
        mockMvc.perform(get("/admin/estimate/cancel/{id}", estimate.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/estimates"));

        then(estimate.getStatus()).isEqualTo(EstimateStatus.CANCEL);
    }

    private Member getMember() {
        MemberDto memberDto = new MemberDto(
                "kim", "1234", "Sam", "abc@gmail.com",
                "01011110000", null, null
        );
        return memberService.join(memberDto);
    }

    private Order getOrder(Member member, String title) {
        OrderDto orderDto = new OrderDto(
                null, title, "내용",
                null, null
        );
        Long id = orderService.order(member.getUsername(), orderDto);
        return orderRepository.getOne(id);
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