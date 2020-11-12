package com.sam.dataviewer.domain;

import com.sam.dataviewer.dto.FigureDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class FigureTest {

    @Test
    public void createFigure() throws Exception {
        //given
        Member member = getMember();
        Order order = getOrder(member);
        Dashboard dashboard = getDashboard(order);

        String title = "그래프";
        String originalFileName = "graph";

        //when
        Figure figure = Figure.createFigure(
                dashboard, title, null,
                originalFileName, null
        );

        //then
        assertThat(dashboard).isEqualTo(figure.getDashboard());
        assertThat(figure).isEqualTo(dashboard.getFigures().get(0));
        assertThat(title).isEqualTo(figure.getTitle());
        assertThat(originalFileName).isEqualTo(figure.getOriginalFileName());
    }

    @Test
    public void toDto() throws Exception {
        //given
        Member member = getMember();
        Order order = getOrder(member);
        Dashboard dashboard = getDashboard(order);
        Figure figure = Figure.createFigure(
                dashboard, "그래프", "내용",
                null, null
        );

        //when
        FigureDto dto = figure.toDto();

        //then
        assertThat(figure.getTitle()).isEqualTo(dto.getTitle());
        assertThat(figure.getDescription()).isEqualTo(dto.getDescription());
    }

    @Test
    public void update() throws Exception {
        //given
        Member member = getMember();
        Order order = getOrder(member);
        Dashboard dashboard = getDashboard(order);
        Figure figure = Figure.createFigure(
                dashboard, "그래프", null,
                "graph", null
        );

        String newTitle = "수정된 그래프";
        String newOriginalFileName = "revised graph";

        //when
        figure.update(
                newTitle, null,
                newOriginalFileName, null
        );

        //then
        assertThat(newTitle).isEqualTo(figure.getTitle());
        assertThat(newOriginalFileName).isEqualTo(figure.getOriginalFileName());
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

    private Dashboard getDashboard(Order order) {
        return Dashboard.createDashboard(
                order, "대시보드", "내용"
        );
    }

}