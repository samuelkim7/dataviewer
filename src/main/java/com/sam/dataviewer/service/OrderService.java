package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.form.OrderForm;
import com.sam.dataviewer.repository.MemberRepository;
import com.sam.dataviewer.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long order(String username, OrderForm form) {
        Member member = memberRepository.findByUsername(username);

        Order order = Order.createOrder(
                member, form.getTitle(),
                form.getContent(), form.getFile()
        );

        orderRepository.save(order);
        return order.getId();
    }

    public List<Order> findOrders(String username) {
        Member member = memberRepository.findByUsername(username);
        return orderRepository.findAllByMember(member);
    }
}
