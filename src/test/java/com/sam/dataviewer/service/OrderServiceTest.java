package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.domain.OrderStatus;
import com.sam.dataviewer.dto.MemberDto;
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

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private MemberService memberService;
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
        orderDto.setTitle("새 의뢰");
        given(memberRepository.findByUsername(member.getUsername())).willReturn(member);

        //when
        Long orderId = orderService.order(member.getUsername(), orderDto);

        //then
        verify(orderRepository, times(1)).save(argumentCaptor.capture());
        then(argumentCaptor.getValue()).isNotNull();
        then(argumentCaptor.getValue().getTitle()).isEqualTo("새 의뢰");

//        assertThat(orderDto.getTitle())
//                .isEqualTo(orderRepository.getOne(orderId).getTitle());
    }

    @Test
    public void findByUsernameTest() throws Exception {
        //given
        Member member = getMember();

        OrderDto orderDto1 = new OrderDto();
        orderDto1.setTitle("의뢰1");
        orderService.order(member.getUsername(), orderDto1);

        OrderDto orderDto2 = new OrderDto();
        orderDto2.setTitle("의뢰2");
        orderService.order(member.getUsername(), orderDto2);

        //when
        List<OrderDto> orderDtos = orderService.findByUsername(member.getUsername());

        //then
        assertThat(2).isEqualTo(orderDtos.size());
        assertThat(orderDto2.getTitle())
                .isEqualTo(orderDtos.get(0).getTitle());
        assertThat(orderDto1.getTitle())
                .isEqualTo(orderDtos.get(1).getTitle());
    }

    @Test
    public void cancelOrderTest() throws Exception {
        //given
        Member member = getMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setTitle("새 의뢰");
        Long orderId = orderService.order(member.getUsername(), orderDto);

        //when
        orderService.cancelOrder(orderId);

        //then
        assertThat(OrderStatus.CANCEL)
                .isEqualTo(orderRepository.getOne(orderId).getStatus());
    }

    @Test
    public void updateOrderTest() throws Exception {
        //given
        Member member = getMember();

        OrderDto orderDto1 = new OrderDto();
        orderDto1.setTitle("웹사이트 분석");
        Long orderId = orderService.order(member.getUsername(), orderDto1);

        OrderDto orderDto2 = new OrderDto();
        orderDto2.setId(orderId);
        orderDto2.setTitle("로그 분석");

        //when
        orderService.updateOrder(orderDto2);

        //then
        assertThat(orderDto2.getTitle())
                .isEqualTo(orderRepository.getOne(orderId).getTitle());
    }

    @Test
    public void startOrderTest() throws Exception {
        //given
        Member member = getMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setTitle("새 의뢰");
        Long orderId = orderService.order(member.getUsername(), orderDto);

        //when
        orderService.startOrder(orderId);

        //then
        assertThat(OrderStatus.ORDER)
                .isEqualTo(orderRepository.getOne(orderId).getStatus());
    }

    private Member getMember() {
        MemberDto memberDto = new MemberDto();
        memberDto.setUsername("kim");
        memberDto.setPassword("1234");
        return memberService.join(memberDto);
    }
}