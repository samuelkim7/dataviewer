package com.sam.dataviewer.repository;

import com.sam.dataviewer.domain.Estimate;
import com.sam.dataviewer.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {
    @Query("SELECT DISTINCT e FROM Estimate as e " +
            "join fetch e.order as o " +
            "join fetch o.member as m " +
            "WHERE m.username = :username")
    List<Estimate> findByUsername(@Param("username") String username);
}
