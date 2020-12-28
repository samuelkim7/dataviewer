package com.sam.dataviewer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.xdevapi.Collection;
import com.sam.dataviewer.domain.Dashboard;
import com.sam.dataviewer.domain.Estimate;
import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.dto.DashboardDto;
import com.sam.dataviewer.dto.MemberDto;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.repository.DashboardRepository;
import com.sam.dataviewer.repository.OrderRepository;
import com.sam.dataviewer.service.DashboardService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@WithMockUser(username = "kim", password = "1234", roles = "USER")
class DashboardControllerTest {

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
    private DashboardService dashboardService;
    @Autowired
    private DashboardRepository dashboardRepository;

    @Test
    @DisplayName("대시보드 리스트 보기")
    public void estimateListTest() throws Exception {
        //given
        Member member = getMember();
        Order order = getOrder(member);
        getDashboard(order, "대시보드1");
        getDashboard(order, "대시보드2");
        MockHttpServletResponse response = mockMvc.perform(get("/dashboards"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/dashboardList"))
                .andExpect(model().attributeExists("dashboardDtos"))
                .andReturn().getResponse();

        then(response.getContentAsString())
                .contains("의뢰", "대시보드1", "대시보드2");
    }

    @Test
    @DisplayName("대시보드 상세보기")
    public void dashboardDetailTest() throws Exception {
        Member member = getMember();
        Order order = getOrder(member);
        Dashboard dashboard = getDashboard(order, "대시보드");
        MockHttpServletResponse response = mockMvc.perform(
                get("/dashboard/dashboardDetail/{id}", dashboard.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard/dashboardDetail"))
                .andExpect(model().attributeExists("dashboardDto", "figureDtos"))
                .andReturn().getResponse();

        then(response.getContentAsString())
                .contains("의뢰", "대시보드", "내용");
    }

    private Member getMember() {
        MemberDto memberDto = new MemberDto();
        memberDto.setUsername("kim");
        memberDto.setPassword("1234");
        return memberService.join(memberDto);
    }

    private Order getOrder(Member member) {
        OrderDto orderDto = new OrderDto();
        orderDto.setTitle("의뢰");
        orderDto.setContent("내용");
        Long id = orderService.order(member.getUsername(), orderDto);
        return orderRepository.getOne(id);
    }

    private Dashboard getDashboard(Order order, String title) {
        DashboardDto dashboardDto = new DashboardDto(
                null, title, "내용",
                null, null
        );
        Long id = dashboardService.create(order.getId(), dashboardDto);
        return dashboardRepository.getOne(id);
    }

}