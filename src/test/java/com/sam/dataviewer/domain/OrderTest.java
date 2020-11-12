package com.sam.dataviewer.domain;

import com.sam.dataviewer.dto.OrderDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

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
        assertThat(member).isEqualTo(order.getMember());
        assertThat(title).isEqualTo(order.getTitle());
        assertThat(content).isEqualTo(order.getContent());
    }

    @Test
    public void toDto() throws Exception {
        //given
        Member member = getMember();
        Order order = Order.createOrder(member, "의뢰", "내용");

        //when
        OrderDto dto = order.toDto();

        //then
        assertThat(order.getTitle()).isEqualTo(dto.getTitle());
        assertThat(order.getContent()).isEqualTo(dto.getContent());
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
        assertThat(newTitle).isEqualTo(order.getTitle());
        assertThat(newContent).isEqualTo(order.getContent());
    }

    @Test
    public void cancel() throws Exception {
        //given
        Member member = getMember();
        Order order = Order.createOrder(member, "의뢰", "내용");

        //when
        order.cancel();

        //then
        assertThat(OrderStatus.CANCEL).isEqualTo(order.getStatus());
    }

    @Test
    public void start() throws Exception {
        //given
        Member member = getMember();
        Order order = Order.createOrder(member, "의뢰", "내용");

        //when
        order.start();

        //then
        assertThat(OrderStatus.ORDER).isEqualTo(order.getStatus());
    }

    private Member getMember() {
        return Member.createMember(
                "kim", null,
                null, null, null,
                null, null
        );
    }
}