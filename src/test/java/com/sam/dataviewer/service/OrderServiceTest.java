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
import java.util.Optional;

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
    @Captor
    private ArgumentCaptor<Order> argumentCaptor;

    @Test
    @DisplayName("의뢰 요청")
    public void orderTest() throws Exception {
        //given
        Member member = getMember();
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
        Member member = getMember();
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
        then(orderDtos.size()).isEqualTo(2);
        then(orderDtos.get(0).getTitle()).isEqualTo("order1");
        then(orderDtos.get(1).getTitle()).isEqualTo("order2");
    }

    @Test
    @DisplayName("의뢰 수정")
    public void updateOrderTest() throws Exception {
        //given
        Member member = getMember();
        Order order = getOrder(member, "order1");

        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setTitle("order2");
        orderDto.setContent("content2");
        Optional<Order> optional = Optional.of(order);
        given(orderRepository.findById(order.getId())).willReturn(optional);

        //when
        orderService.updateOrder(orderDto);

        //then
        then(order.getTitle()).isEqualTo("order2");
        then(order.getContent()).isEqualTo("content2");
    }

    @Test
    @DisplayName("의뢰 취소")
    public void cancelOrderTest() throws Exception {
        //given
        Member member = getMember();
        Order order = getOrder(member, "order");
        Optional<Order> optional = Optional.of(order);
        given(orderRepository.findById(order.getId())).willReturn(optional);

        //when
        orderService.cancelOrder(order.getId());

        //then
        then(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @Test
    @DisplayName("의뢰에 따른 분석 시작")
    public void startOrderTest() throws Exception {
        //given
        Member member = getMember();
        Order order = getOrder(member, "order");
        Optional<Order> optional = Optional.of(order);
        given(orderRepository.findById(order.getId())).willReturn(optional);

        //when
        orderService.startOrder(order.getId());

        //then
        then(order.getStatus()).isEqualTo(OrderStatus.ORDER);
    }

    private Member getMember() {
        return Member.createMember(
                "kim", "1234", null, null,
                null, null, null);
    }

    private Order getOrder(Member member, String title) {
        return Order.createOrder(member, title, "content");
    }
}