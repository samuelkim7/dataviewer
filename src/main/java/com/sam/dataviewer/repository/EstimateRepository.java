package com.sam.dataviewer.repository;

import com.sam.dataviewer.domain.Estimate;
import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {
    @Query("SELECT e FROM Estimate as e" +
            " join e.order as o" +
            " join o.member as m" +
            " WHERE m.username = :username" +
            " ORDER BY e.id DESC")
    List<Estimate> findByUsername(@Param("username") String username);

    List<Estimate> findByOrderByIdDesc();
}
