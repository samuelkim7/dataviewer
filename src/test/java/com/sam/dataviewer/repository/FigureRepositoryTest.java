package com.sam.dataviewer.repository;

import com.sam.dataviewer.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest(showSql = false)
@Transactional
class FigureRepositoryTest {

    @Autowired
    private FigureRepository figureRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("대시보드 id로 정렬하여 모두 조회")
    public void findOrderByDashboardTest() throws Exception {
        //given
        Member member = getMember();
        testEntityManager.persist(member);
        Order order = getOrder(member);
        testEntityManager.persist(order);
        Dashboard dashboard1 = getDashboard(order, "dashboard1");
        Dashboard dashboard2 = getDashboard(order, "dashboard2");
        testEntityManager.persist(dashboard1);
        testEntityManager.persist(dashboard2);

        IntStream.range(0, 5).forEach(i -> {
            Figure figure = getFigure(dashboard1);

            testEntityManager.persist(figure);
            testEntityManager.flush();
            testEntityManager.clear();
        });

        IntStream.range(0, 5).forEach(i -> {
            Figure figure = getFigure(dashboard2);

            testEntityManager.persist(figure);
            testEntityManager.flush();
            testEntityManager.clear();
        });

        //when
        List<Figure> figures = figureRepository.findOrderByDashboard();

        //then
        then(figures).hasSize(10);
        then(figures.get(0).getTitle()).isEqualTo("figure");
        then(figures.get(0).getDashboard().getTitle()).isEqualTo("dashboard2");
        then(figures.get(4).getDashboard().getTitle()).isEqualTo("dashboard2");
        then(figures.get(5).getDashboard().getTitle()).isEqualTo("dashboard1");
    }

    @Test
    @DisplayName("대시보드 id로 조회")
    public void findByDashboardIdTest() throws Exception {
        //given
        Member member = getMember();
        testEntityManager.persist(member);
        Order order = getOrder(member);
        testEntityManager.persist(order);
        Dashboard dashboard1 = getDashboard(order, "dashboard1");
        Dashboard dashboard2 = getDashboard(order, "dashboard2");
        testEntityManager.persist(dashboard1);
        testEntityManager.persist(dashboard2);

        IntStream.range(0, 5).forEach(i -> {
            Figure figure = getFigure(dashboard1);

            testEntityManager.persist(figure);
            testEntityManager.flush();
            testEntityManager.clear();
        });

        IntStream.range(0, 5).forEach(i -> {
            Figure figure = getFigure(dashboard2);

            testEntityManager.persist(figure);
            testEntityManager.flush();
            testEntityManager.clear();
        });

        //when
        List<Figure> figures = figureRepository.findByDashboardId(dashboard1.getId());

        //then
        then(figures).hasSize(5);
        then(figures.get(0).getDashboard().getTitle()).isEqualTo("dashboard1");
    }

    private Figure getFigure(Dashboard dashboard) {
        return Figure.createFigure(
                dashboard, "figure", "description",
                null, null);
    }

    private Dashboard getDashboard(Order order, String title) {
        return Dashboard.createDashboard(order, title, "content");
    }

    private Member getMember() {
        return Member.createMember(
                "kim", null, null,
                null, null, null, null);
    }

    private Order getOrder(Member member) {
        return Order.createOrder(member, "order", "content");
    }


}