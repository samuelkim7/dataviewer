package com.sam.dataviewer.repository;

import static org.assertj.core.api.BDDAssertions.then;

import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(showSql = false)
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("회원명으로 조회")
    public void findByUsernameTest() throws Exception {
        //given
        Member member = getMember();
        testEntityManager.persist(member);
        testEntityManager.flush();
        testEntityManager.clear();

        //when
        Member memberFound = memberRepository.findByUsername("kim");

        //then
        then(memberFound.getUsername()).isEqualTo("kim");
        then(memberFound.getPassword()).isEqualTo("1234");
        then(memberFound.getName()).isEqualTo("sam");
    }

    @Test
    @DisplayName("주문 id로 조회")
    public void findByOrderIdTest() throws Exception {
        //given
        Member member = getMember();
        testEntityManager.persist(member);

        Order order1 = getOrder(member, "order1");
        Order order2 = getOrder(member, "order2");
        testEntityManager.persist(order1);
        testEntityManager.persist(order2);
        testEntityManager.flush();
        testEntityManager.clear();

        //when
        Member memberFound = memberRepository.findByOrderId(order1.getId());

        //then
        then(memberFound.getUsername()).isEqualTo("kim");
        then(memberFound.getOrders()).hasSize(2);
        then(memberFound.getOrders().get(0).getTitle()).isEqualTo("order1");
        then(memberFound.getOrders().get(1).getTitle()).isEqualTo("order2");
    }

    private Member getMember() {
        return Member.createMember(
                "kim", "1234", "sam",
                null, null, null, null);
    }

    private Order getOrder(Member member, String title) {
        return Order.createOrder(member, title, "content");
    }
}