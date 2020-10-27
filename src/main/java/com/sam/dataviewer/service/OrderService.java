package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.form.OrderForm;
import com.sam.dataviewer.repository.MemberRepository;
import com.sam.dataviewer.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    /* 의뢰 전체 조회 */
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    /* 의뢰 한 건 조회 */
    public OrderForm findOne(Long orderId){
        Order order = orderRepository.getOne(orderId);
        return order.toForm();
    }

    /* 회원 아이디 별 의뢰 전체 조회 */
    public List<OrderForm> findOrdersByUsername(String username) {
        Member member = memberRepository.findByUsername(username);
        List<Order> orders = orderRepository.findByMemberOrderByIdDesc(member);
        List<OrderForm> orderForms = new ArrayList<>();
        for (Order order : orders) {
            OrderForm form = order.toForm();
            orderForms.add(form);
        }
        return orderForms;
    }

    /* 의뢰 취소 */
    @Transactional
    public void cancelOrder(Long id) {
        Order order = orderRepository.getOne(id);
        order.cancel();
    }

    /* 의뢰 수정 */
    @Transactional
    public void updateOrder(OrderForm form) {
        Order order = orderRepository.getOne(form.getId());
        order.update(form.getTitle(), form.getContent(), form.getFile());
    }
}
