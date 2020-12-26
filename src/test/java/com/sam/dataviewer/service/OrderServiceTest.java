package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.domain.OrderStatus;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.repository.MemberRepository;
import com.sam.dataviewer.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private Member member;
    @Captor
    private ArgumentCaptor<Order> argumentCaptor;

    @Test
    @DisplayName("의뢰 요청")
    public void orderTest() throws Exception {
        //given
        OrderDto orderDto = new OrderDto();
        orderDto.setTitle("order");
        orderDto.setContent("content");
        given(memberRepository.findByUsername(member.getUsername())).willReturn(member);

        //when
        orderService.order(member.getUsername(), orderDto);

        //then
        verify(orderRepository, times(1)).save(argumentCaptor.capture());
        then(argumentCaptor.getValue()).isNotNull();
        then(argumentCaptor.getValue().getTitle()).isEqualTo("order");
        then(argumentCaptor.getValue().getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("회원 아이디로 조회")
    public void findByUsernameTest() throws Exception {
        //given
        Order order1 = getOrder(member, "order1");
        Order order2 = getOrder(member, "order2");
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        given(memberRepository.findByUsername(member.getUsername())).willReturn(member);
        given(orderRepository.findByMemberOrderByIdDesc(member)).willReturn(orders);

        //when
        List<OrderDto> orderDtos = orderService.findByUsername(member.getUsername());

        //then
        then(2).isEqualTo(orderDtos.size());
        then("order1").isEqualTo(orderDtos.get(0).getTitle());
        then("order2").isEqualTo(orderDtos.get(1).getTitle());
    }

    @Test
    @DisplayName("의뢰 수정")
    public void updateOrderTest() throws Exception {
        //given
        Order order = getOrder(member, "order1");

        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setTitle("order2");
        orderDto.setContent("content2");
        given(orderRepository.getOne(orderDto.getId())).willReturn(order);

        //when
        orderService.updateOrder(orderDto);

        //then
        then("order2").isEqualTo(order.getTitle());
        then("content2").isEqualTo(order.getContent());
    }

    @Test
    @DisplayName("의뢰 취소")
    public void cancelOrderTest() throws Exception {
        //given
        Order order = getOrder(member, "order");
        given(orderRepository.getOne(order.getId())).willReturn(order);

        //when
        orderService.cancelOrder(order.getId());

        //then
        then(OrderStatus.CANCEL).isEqualTo(order.getStatus());
    }

    @Test
    @DisplayName("의뢰에 따른 분석 시작")
    public void startOrderTest() throws Exception {
        //given
        Order order = getOrder(member, "order");
        given(orderRepository.getOne(order.getId())).willReturn(order);

        //when
        orderService.startOrder(order.getId());

        //then
        then(OrderStatus.ORDER).isEqualTo(order.getStatus());
    }

    private Order getOrder(Member member, String title) {
        return Order.createOrder(member, title, "content");
    }
}