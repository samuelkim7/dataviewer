package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Dashboard;
import com.sam.dataviewer.dto.DashboardDto;
import com.sam.dataviewer.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    @Transactional
    public Long create(DashboardDto dto) {
        Dashboard dashboard = Dashboard.createDashboard(
                dto.getTitle(), dto.getContent()
        );
        dashboardRepository.save(dashboard);
        return dashboard.getId();
    }
}
