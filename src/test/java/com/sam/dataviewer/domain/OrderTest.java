package com.sam.dataviewer.domain;

import com.sam.dataviewer.dto.OrderDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class OrderTest {

    @Test
    public void createOrder() throws Exception {
        //given
        Member member = getMember();

        String title = "새 의뢰";
        String content = "웹사이트 분석 의뢰";

        //when
        Order order = Order.createOrder(member, title, content);

        //then
        then(order.getMember()).isEqualTo(member);
        then(member.getOrders().get(0)).isEqualTo(order);
        then(order.getTitle()).isEqualTo(title);
        then(order.getContent()).isEqualTo(content);
    }

    @Test
    public void toDto() throws Exception {
        //given
        Member member = getMember();
        Order order = Order.createOrder(member, "의뢰", "내용");

        //when
        OrderDto dto = order.toDto();

        //then
        then(order.getTitle()).isEqualTo(dto.getTitle());
        then(order.getContent()).isEqualTo(dto.getContent());
    }

    @Test
    public void update() throws Exception {
        //given
        Member member = getMember();
        Order order = Order.createOrder(member, "의뢰", "내용");

        String newTitle = "수정된 의뢰";
        String newContent = "수정된 내용";

        //when
        order.update(newTitle, newContent);

        //then
        then(order.getTitle()).isEqualTo(newTitle);
        then(order.getContent()).isEqualTo(newContent);
    }

    @Test
    public void cancel() throws Exception {
        //given
        Member member = getMember();
        Order order = Order.createOrder(member, "의뢰", "내용");

        //when
        order.cancel();

        //then
        then(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @Test
    public void start() throws Exception {
        //given
        Member member = getMember();
        Order order = Order.createOrder(member, "의뢰", "내용");

        //when
        order.start();

        //then
        then(order.getStatus()).isEqualTo(OrderStatus.ORDER);
    }

    private Member getMember() {
        return Member.createMember(
                "kim", null,
                null, null, null,
                null, null
        );
    }
}