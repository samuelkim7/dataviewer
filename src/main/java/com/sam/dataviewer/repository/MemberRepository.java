package com.sam.dataviewer.repository;

import com.sam.dataviewer.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);

    @Query("SELECT m FROM Member m" +
            " JOIN m.orders o" +
            " WHERE o.id = :id ")
    Member findByOrderId(@Param("id") Long orderId);
}
