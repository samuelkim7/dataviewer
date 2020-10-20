package com.sam.dataviewer.repository;

import com.sam.dataviewer.domain.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
}
