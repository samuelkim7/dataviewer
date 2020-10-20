package com.sam.dataviewer.repository;

import com.sam.dataviewer.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
