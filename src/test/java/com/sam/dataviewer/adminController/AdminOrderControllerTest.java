package com.sam.dataviewer.adminController;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@WithMockUser(roles = "ADMIN")
class AdminOrderControllerTest {

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
    @DisplayName("의뢰 리스트 보기")
    public void orderListTest() throws Exception {
        Member member = getMember();
        Order order1 = getOrder(member, "의뢰1");
        Order order2 = getOrder(member, "의뢰2");
        MockHttpServletResponse response =
                mockMvc.perform(get("/admin/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/order/orderList"))
                .andExpect(model().attributeExists("orderDtos"))
                .andReturn().getResponse();

        then(response.getContentAsString())
                .contains("의뢰1", "의뢰2");
    }

    @Test
    @DisplayName("의뢰 상세보기")
    public void orderDetailTest() throws Exception {
        Member member = getMember();
        Order order = getOrder(member, "의뢰");
        MockHttpServletResponse response = mockMvc.perform(
                get("/admin/order/orderDetail/{id}", order.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/order/orderDetail"))
                .andExpect(model().attributeExists("orderDto", "memberDto", "fileDtos"))
                .andReturn().getResponse();

        then(response.getContentAsString())
                .contains("의뢰", "내용");
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

}