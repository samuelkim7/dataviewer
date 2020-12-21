package com.sam.dataviewer.repository;

import static org.assertj.core.api.BDDAssertions.then;

import com.sam.dataviewer.domain.Estimate;
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

@DataJpaTest(showSql = false)
@Transactional
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("회원으로 조회시 테스트")
    public void findByMemberOrderByIdDescTest() throws Exception {
        //given
        Member member = getMember();
        testEntityManager.persist(member);

        IntStream.range(0, 5).forEach(i -> {
            Order order = Order.createOrder(member, "order", "content");

            testEntityManager.persist(order);
            testEntityManager.flush();
            testEntityManager.clear();
            }
        );

        //when
        List<Order> orders = orderRepository.findByMemberOrderByIdDesc(member);

        //then
        then(orders).hasSize(5);
        then(orders.get(0).getMember().getUsername()).isEqualTo("kim");
        then(orders.get(0).getTitle()).isEqualTo("order");
        then(orders.get(0).getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("id 내림차순 조회")
    public void findByOrderByIdDescTest() throws Exception {
        //given
        Member member = getMember();
        testEntityManager.persist(member);

        IntStream.range(0, 5).forEach(i -> {
            Order order = Order.createOrder(member, "order", "content");

            testEntityManager.persist(order);
            testEntityManager.flush();
            testEntityManager.clear();
            }
        );

        //when
        List<Order> orders = orderRepository.findByOrderByIdDesc();

        //then
        // member의 id가 1L, order의 id는 2L부터 6L까지로 설정됨
        then(orders).hasSize(5);
        then(orders.get(0).getId()).isEqualTo(6L);
        then(orders.get(4).getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("견적 id로 조회")
    public void findByEstimateIdTest() throws Exception {
        //given
        Member member = getMember();
        testEntityManager.persist(member);

        Order order = Order.createOrder(member, "order", "content");
        testEntityManager.persist(order);

        Estimate estimate = getEstimate(order);
        testEntityManager.persist(estimate);

        testEntityManager.flush();
        testEntityManager.clear();

        //when
        Order orderGot = orderRepository.findByEstimateId(estimate.getId());

        //then
        then(orderGot.getTitle()).isEqualTo("order");
        then(orderGot.getEstimates().size()).isEqualTo(1);
        then(orderGot.getEstimates().get(0).getTitle()).isEqualTo("estimate");
    }

    private Estimate getEstimate(Order order) {
        return Estimate.createEstimate(order, "estimate", null, null, null);
    }

    private Member getMember() {
        return Member.createMember(
                "kim", null, null,
                null, null, null, null);
    }

}