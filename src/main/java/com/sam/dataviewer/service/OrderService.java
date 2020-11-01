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
                dto.getContent(), dto.getFile()
        );

        orderRepository.save(order);
        return order.getId();
    }

    /* 의뢰 전체 조회 for ADMIN */
    public List<OrderDto> findAll() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            orderDtos.add(order.toDto());
        }
        return orderDtos;
    }

    /* 의뢰 한 건 조회 */
    public OrderDto findOne(Long id){
        Order order = orderRepository.getOne(id);
        return order.toDto();
    }

    /* 회원 아이디 별 의뢰 전체 조회 for USER */
    public List<OrderDto> findOrdersByUsername(String username) {
        Member member = memberRepository.findByUsername(username);
        List<Order> orders = orderRepository.findByMemberOrderByIdDesc(member);
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            orderDtos.add(order.toDto());
        }
        return orderDtos;
    }

    /* 의뢰 취소 */
    @Transactional
    public void cancelOrder(Long id) {
        Order order = orderRepository.getOne(id);
        order.cancel();
    }

    /* 의뢰 수정 */
    @Transactional
    public void updateOrder(OrderDto dto) {
        Order order = orderRepository.getOne(dto.getId());
        order.update(dto.getTitle(), dto.getContent(), dto.getFile());
    }

    public OrderDto findOrderByEstimateId(Long estimateId) {
        Order order = orderRepository.findByEstimateId(estimateId);
        return order.toDto();
    }
}
