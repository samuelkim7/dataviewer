package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.OrderStatus;
import com.sam.dataviewer.dto.MemberDto;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired MemberService memberService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 의뢰_신청() throws Exception {
        //given
        Member member = getMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setTitle("새 의뢰");

        //when
        Long orderId = orderService.order(member.getUsername(), orderDto);

        //then
        assertThat(orderDto.getTitle())
                .isEqualTo(orderRepository.getOne(orderId).getTitle());
    }

    @Test
    public void 회원_아이디별_의뢰_전체_조회() throws Exception {
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
    public void 의뢰_취소() throws Exception {
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
    public void 의뢰_수정() throws Exception {
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
    public void 의뢰_시작() throws Exception {
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