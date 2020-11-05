package com.sam.dataviewer.repository;

import com.sam.dataviewer.domain.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
    List<Dashboard> findByOrderByIdDesc();
}
