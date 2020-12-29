package com.sam.dataviewer.adminController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam.dataviewer.domain.*;
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
class AdminDashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private DashboardRepository dashboardRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("대시보드 생성 폼")
    public void createFormTest() throws Exception {
        Member member = getMember();
        getOrder(member, "의뢰1");
        getOrder(member, "의뢰2");
        MockHttpServletResponse response =
                mockMvc.perform(get("/admin/dashboard/new"))
                        .andExpect(status().isOk())
                        .andExpect(view().name("admin/dashboard/createDashboardForm"))
                        .andExpect(model().attributeExists("orders", "dashboardDto"))
                        .andReturn().getResponse();

        then(response.getContentAsString()).contains("의뢰1", "의뢰2");
    }

    @Test
    @DisplayName("대시보드 생성")
    public void createDashboardTest() throws Exception {
        Member member = getMember();
        Order order = getOrder(member, "의뢰");
        DashboardDto dto = new DashboardDto(
                null, "대시보드", "내용",
                null, null
        );
        mockMvc.perform(post("/admin/dashboard/new").with(csrf())
                .param("orderId", order.getId().toString())
                .param("title", dto.getTitle())
                .param("content", dto.getContent()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboards"));

        List<Dashboard> dashboards = order.getDashboards();
        then(dashboards.get(0).getTitle()).isEqualTo("대시보드");
        then(dashboards.get(0).getContent()).isEqualTo("내용");
        then(dashboards.get(0).getOrder()).isEqualTo(order);
    }

    @Test
    @DisplayName("대시보드 리스트 보기")
    public void dashboardListTest() throws Exception {
        Member member = getMember();
        Order order = getOrder(member, "의뢰");
        getDashboard(order.getId(), "대시보드1");
        getDashboard(order.getId(), "대시보드2");
        MockHttpServletResponse response = mockMvc.perform(get("/admin/dashboards"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard/dashboardList"))
                .andExpect(model().attributeExists("dashboardDtos"))
                .andReturn().getResponse();

        then(response.getContentAsString())
                .contains("의뢰", "대시보드1", "대시보드2");
    }

    @Test
    @DisplayName("대시보드 상세보기")
    public void dashboardDetailTest() throws Exception {
        Member member = getMember();
        Order order = getOrder(member, "의뢰");
        Dashboard dashboard = getDashboard(order.getId(), "대시보드");
        MockHttpServletResponse response = mockMvc.perform(
                get("/admin/dashboard/dashboardDetail/{id}", dashboard.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard/dashboardDetail"))
                .andExpect(model().attributeExists("dashboardDto", "figureDtos"))
                .andReturn().getResponse();

        then(response.getContentAsString())
                .contains("의뢰", "대시보드", "내용");
    }

    @Test
    @DisplayName("대시보드 수정하기")
    public void updateDashboardTest() throws Exception {
        Member member = getMember();
        Order order = getOrder(member, "의뢰");
        Dashboard dashboard = getDashboard(order.getId(), "대시보드");
        DashboardDto dto = new DashboardDto(
                dashboard.getId(), "대시보드1", "내용1",
                null, null
        );
        mockMvc.perform(post("/admin/dashboard/update").with(csrf())
                .param("id", dto.getId().toString())
                .param("title", dto.getTitle())
                .param("content", dto.getContent()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboards"));

        then(dashboard.getTitle()).isEqualTo("대시보드1");
        then(dashboard.getContent()).isEqualTo("내용1");
    }

    @Test
    @DisplayName("대시보드 삭제하기")
    public void deleteDashboardTest() throws Exception {
        Member member = getMember();
        Order order = getOrder(member, "의뢰");
        Dashboard dashboard = getDashboard(order.getId(), "대시보드");
        Long id = dashboard.getId();
        mockMvc.perform(get("/admin/dashboard/delete/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboards"));

        then(dashboardRepository.findById(id)).isEmpty();
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

    private Dashboard getDashboard(Long orderId, String title) {
        DashboardDto dashboardDto = new DashboardDto(
                null, title, "내용",
                null, null
        );
        Long id = dashboardService.create(orderId, dashboardDto);
        return dashboardRepository.getOne(id);
    }

}