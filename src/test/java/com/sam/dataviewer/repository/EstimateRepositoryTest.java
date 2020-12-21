package com.sam.dataviewer.repository;

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

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest(showSql = false)
@Transactional
class EstimateRepositoryTest {

    @Autowired
    private EstimateRepository estimateRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("id 내림차순 조회")
    public void findByOrderByIdDescTest() throws Exception {
        //given
        Member member = getMember();
        testEntityManager.persist(member);
        Order order = getOrder(member);
        testEntityManager.persist(order);

        IntStream.range(0, 5).forEach(i -> {
            Estimate estimate = Estimate.createEstimate(
                    order, "estimate", null, null, null);

            testEntityManager.persist(estimate);
            testEntityManager.flush();
            testEntityManager.clear();
            }
        );

        //when
        List<Estimate> estimates = estimateRepository.findByOrderByIdDesc();

        //then
        then(estimates).hasSize(5);
        then(estimates.get(0).getId() - estimates.get(4).getId()).isEqualTo(4L);
        then(estimates.get(0).getTitle()).isEqualTo("estimate");
        then(estimates.get(0).getOrder().getTitle()).isEqualTo("order");
    }

    @Test
    @DisplayName("회원명으로 조회")
    public void findByUsernameTest() throws Exception {
        //given
        Member member = getMember();
        testEntityManager.persist(member);
        Order order = getOrder(member);
        testEntityManager.persist(order);

        IntStream.range(0, 5).forEach(i -> {
                    Estimate estimate = Estimate.createEstimate(
                            order, "estimate", null, null, null);

                    testEntityManager.persist(estimate);
                    testEntityManager.flush();
                    testEntityManager.clear();
                }
        );

        //when
        List<Estimate> estimates = estimateRepository.findByUsername(member.getUsername());

        //then
        then(estimates).hasSize(5);
        then(estimates.get(0).getOrder().getMember().getUsername()).isEqualTo("kim");
    }

    private Order getOrder(Member member) {
        return Order.createOrder(member, "order", "content");
    }

    private Member getMember() {
        return Member.createMember(
                "kim", null, null,
                null, null, null, null);
    }

}