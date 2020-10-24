package com.sam.dataviewer.repository;

import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMemberOrderByIdDesc(Member member);
}
