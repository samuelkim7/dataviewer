package com.sam.dataviewer.repository;

import com.sam.dataviewer.domain.Estimate;
import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMemberOrderByIdDesc(Member member);

    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN o.estimates e " +
            "WHERE e.id = :id")
    Order findByEstimateId(@Param("id") Long estimateId);

    List<Order> findByOrderByIdDesc();
}
