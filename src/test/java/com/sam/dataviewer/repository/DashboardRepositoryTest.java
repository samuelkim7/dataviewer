package com.sam.dataviewer.repository;

import com.sam.dataviewer.domain.Dashboard;
import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
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
class DashboardRepositoryTest {

    @Autowired
    private DashboardRepository dashboardRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("id로 내림차순 정렬하여 조회")
    public void findByOrderByIdDescTest() throws Exception {
        //given
        Member member = getMember("kim");
        testEntityManager.persist(member);
        Order order = getOrder(member);
        testEntityManager.persist(order);

        IntStream.range(0, 5).forEach(i -> {
            Dashboard dashboard = getDashboard(order, "dashboard" + (i+1));

            testEntityManager.persist(dashboard);
            testEntityManager.flush();
            testEntityManager.clear();
        });

        //when
        List<Dashboard> dashboards = dashboardRepository.findByOrderByIdDesc();

        //then
        then(dashboards).hasSize(5);
        then(dashboards.get(0).getTitle()).isEqualTo("dashboard5");
        then(dashboards.get(4).getTitle()).isEqualTo("dashboard1");
    }

    @Test
    @DisplayName("회원명으로 조회")
    public void findByUsernameTest() throws Exception {
        //given
        Member member1 = getMember("kim");
        Member member2 = getMember("sam");
        testEntityManager.persist(member1);
        testEntityManager.persist(member2);
        Order order1 = getOrder(member1);
        Order order2 = getOrder(member2);
        testEntityManager.persist(order1);
        testEntityManager.persist(order2);

        IntStream.range(0, 5).forEach(i -> {
            Dashboard dashboard = getDashboard(order1, "dashboard");

            testEntityManager.persist(dashboard);
            testEntityManager.flush();
            testEntityManager.clear();
        });

        IntStream.range(0, 5).forEach(i -> {
            Dashboard dashboard = getDashboard(order2, "dashboard");

            testEntityManager.persist(dashboard);
            testEntityManager.flush();
            testEntityManager.clear();
        });

        //when
        List<Dashboard> dashboards = dashboardRepository.findByUsername("kim");

        //then
        then(dashboards).hasSize(5);
        then(dashboards.get(0).getOrder().getMember().getUsername()).isEqualTo("kim");
    }

    private Dashboard getDashboard(Order order, String title) {
        return Dashboard.createDashboard(order, title, "content");
    }

    private Member getMember(String username) {
        return Member.createMember(
                username, null, null,
                null, null, null, null);
    }

    private Order getOrder(Member member) {
        return Order.createOrder(member, "order", "content");
    }


}