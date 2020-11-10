package com.sam.dataviewer.repository;

import com.sam.dataviewer.domain.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
    List<Dashboard> findByOrderByIdDesc();

    @Query("SELECT d FROM Dashboard as d" +
            " join d.order as o" +
            " join o.member as m" +
            " WHERE m.username = :username")
    List<Dashboard> findByUsername(@Param("username") String username);
}
