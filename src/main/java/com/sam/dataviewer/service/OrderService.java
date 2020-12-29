package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Estimate;
import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.repository.EstimateRepository;
import com.sam.dataviewer.repository.MemberRepository;
import com.sam.dataviewer.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long order(String username, OrderDto dto) {
        Member member = memberRepository.findByUsername(username);

        Order order = Order.createOrder(
                member, dto.getTitle(),
                dto.getContent()
        );

        orderRepository.save(order);
        return order.getId();
    }

    /* 의뢰 전체 조회 for ADMIN */
    public List<OrderDto> findAll() {
        List<Order> orders = orderRepository.findByOrderByIdDesc();
        return orders.stream().map(o -> o.toDto()).collect(Collectors.toList());
    }

    /* 의뢰 한 건 조회 */
    public OrderDto findOne(Long id){
        Optional<Order> optional = orderRepository.findById(id);
        return optional.map(o -> o.toDto()).orElse(new OrderDto());
    }

    /* 회원 아이디별 의뢰 전체 조회 for USER */
    public List<OrderDto> findByUsername(String username) {
        Member member = memberRepository.findByUsername(username);
        List<Order> orders = orderRepository.findByMemberOrderByIdDesc(member);
        return orders.stream().map(o -> o.toDto()).collect(Collectors.toList());
    }

    /* 의뢰 취소 */
    @Transactional
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.cancel();
        }
    }

    /* 의뢰 수정 */
    @Transactional
    public void updateOrder(OrderDto dto) {
        Order order = orderRepository.findById(dto.getId()).orElse(null);
        if (order != null) {
            order.update(dto.getTitle(), dto.getContent());
        }
    }

    public OrderDto findOrderByEstimateId(Long estimateId) {
        Order order = orderRepository.findByEstimateId(estimateId);
        return order.toDto();
    }

    /* 의뢰 시작 */
    @Transactional
    public void startOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.start();
        }
    }
}
