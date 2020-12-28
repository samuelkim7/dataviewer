package com.sam.dataviewer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam.dataviewer.domain.Dashboard;
import com.sam.dataviewer.domain.Figure;
import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.dto.DashboardDto;
import com.sam.dataviewer.dto.FigureDto;
import com.sam.dataviewer.dto.MemberDto;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.repository.DashboardRepository;
import com.sam.dataviewer.repository.FigureRepository;
import com.sam.dataviewer.service.DashboardService;
import com.sam.dataviewer.service.FigureService;
import com.sam.dataviewer.service.MemberService;
import com.sam.dataviewer.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@WithMockUser(username = "kim", password = "1234", roles = "USER")
class FigureControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private FigureService figureService;
    @Autowired
    private FigureRepository figureRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DashboardService dashboardService;

    @Test
    @DisplayName("figure 상세보기")
    public void figureViewTest() throws Exception {
        Long orderId = getOrder();
        Figure figure = getFigure(orderId);
        MockHttpServletResponse response = mockMvc.perform(
                get("/figure/figureView/{id}", figure.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("figure/figureView"))
                .andExpect(model().attributeExists("figureDto"))
                .andDo(print())
                .andReturn().getResponse();

        then(response.getContentAsString())
                .contains("figure", "설명");
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

    private Figure getFigure(Long orderId) {
        DashboardDto dashboardDto = new DashboardDto(
                null, "대시보드", "내용",
                null, null
        );
        Long dashboardId = dashboardService.create(orderId, dashboardDto);

        FigureDto figureDto = new FigureDto(
                null, "figure", "설명",
                null, null, null,
                null, null, null
        );
        Long id = figureService.create(
                dashboardId, figureDto, null, null
        );
        return figureRepository.getOne(id);
    }
}