package com.sam.dataviewer.domain;

import com.sam.dataviewer.dto.DashboardDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DashboardTest {

    @Test
    public void createDashboard() throws Exception {
        //given
        Member member = getMember();
        Order order = getOrder(member);

        String title = "대시보드";
        String content = "내용";

        //when
        Dashboard dashboard = Dashboard.createDashboard(order, title, content);

        //then
        assertThat(order).isEqualTo(dashboard.getOrder());
        assertThat(dashboard).isEqualTo(order.getDashboards().get(0));
        assertThat(title).isEqualTo(dashboard.getTitle());
        assertThat(content).isEqualTo(dashboard.getContent());
    }

    @Test
    public void toDto() throws Exception {
        //given
        Member member = getMember();
        Order order = getOrder(member);
        Dashboard dashboard = Dashboard.createDashboard(
                order, "대시보드", "내용"
        );

        //when
        DashboardDto dto = dashboard.toDto();

        //then
        assertThat(dashboard.getTitle()).isEqualTo(dto.getTitle());
        assertThat(dashboard.getContent()).isEqualTo(dto.getContent());
    }

    @Test
    public void update() throws Exception {
        //given
        Member member = getMember();
        Order order = getOrder(member);
        Dashboard dashboard = Dashboard.createDashboard(
                order, "대시보드", "내용"
        );

        String newTitle = "수정된 대시보드";
        String newContent = "수정된 내용";

        //when
        dashboard.update(newTitle, newContent);

        //then
        assertThat(newTitle).isEqualTo(dashboard.getTitle());
        assertThat(newContent).isEqualTo(dashboard.getContent());
    }

    private Member getMember() {
        return Member.createMember(
                "kim", null,
                null, null, null,
                null, null
        );
    }

    private Order getOrder(Member member) {
        return Order.createOrder(member, "의뢰", "내용");
    }

}