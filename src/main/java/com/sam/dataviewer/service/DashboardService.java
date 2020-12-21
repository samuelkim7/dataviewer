package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Dashboard;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.dto.DashboardDto;
import com.sam.dataviewer.repository.DashboardRepository;
import com.sam.dataviewer.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Long create(Long orderId, DashboardDto dto) {
        Order order = orderRepository.getOne(orderId);
        Dashboard dashboard = Dashboard.createDashboard(
                order, dto.getTitle(), dto.getContent()
        );
        dashboardRepository.save(dashboard);
        return dashboard.getId();
    }

    /* Dashboard 전체 조회 for ADMIN */
    public List<DashboardDto> findAll() {
        List<Dashboard> dashboards = dashboardRepository.findByOrderByIdDesc();
        return dashboards.stream().map(d -> d.toDto()).collect(Collectors.toList());
    }

    /* Dashboard 하나 조회 */
    public DashboardDto findOne(Long id) {
        Dashboard dashboard = dashboardRepository.getOne(id);
        return dashboard.toDto();
    }

    /* Dashboard 수정 */
    @Transactional
    public void updateDashboard(DashboardDto dto) {
        Dashboard dashboard = dashboardRepository.getOne(dto.getId());
        dashboard.update(dto.getTitle(), dto.getContent());
    }

    /* Dashboard 삭제 */
    @Transactional
    public void deleteDashboard(Long id) {
        dashboardRepository.deleteById(id);
    }

    /* 회원명으로 조회 for Member */
    public List<DashboardDto> findByUsername(String username) {
        List<Dashboard> dashboards = dashboardRepository.findByUsername(username);
        return dashboards.stream().map(d -> d.toDto()).collect(Collectors.toList());
    }
}
