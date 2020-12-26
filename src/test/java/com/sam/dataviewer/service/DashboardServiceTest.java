package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Dashboard;
import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.dto.DashboardDto;
import com.sam.dataviewer.repository.DashboardRepository;
import com.sam.dataviewer.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @InjectMocks
    private DashboardService dashboardService;
    @Mock
    private DashboardRepository dashboardRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private Member member;
    @Mock
    private Order order;
    @Captor
    private ArgumentCaptor<Dashboard> argumentCaptor;

    @Test
    @DisplayName("대시보드 생성")
    public void createTest() throws Exception {
        //given
        DashboardDto dashboardDto = new DashboardDto();
        dashboardDto.setTitle("dashboard");
        dashboardDto.setContent("content");
        given(orderRepository.getOne(order.getId())).willReturn(order);

        //when
        dashboardService.create(order.getId(), dashboardDto);

        //then
        verify(dashboardRepository, times(1)).save(argumentCaptor.capture());
        then(argumentCaptor.getValue()).isNotNull();
        then(argumentCaptor.getValue().getTitle()).isEqualTo("dashboard");
        then(argumentCaptor.getValue().getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("회원 아이디로 조회")
    public void findByUsernameTest() throws Exception {
        //given
        Dashboard dashboard1 = getDashboard(order, "dashboard1");
        Dashboard dashboard2 = getDashboard(order, "dashboard2");
        List<Dashboard> dashboards = new ArrayList<>();
        dashboards.add(dashboard1);
        dashboards.add(dashboard2);
        given(dashboardRepository.findByUsername(member.getUsername()))
                .willReturn(dashboards);

        //when
        List<DashboardDto> dashboardDtos = dashboardService.findByUsername(member.getUsername());

        //then
        then(2).isEqualTo(dashboardDtos.size());
        then("dashboard1").isEqualTo(dashboardDtos.get(0).getTitle());
        then("dashboard2").isEqualTo(dashboardDtos.get(1).getTitle());
    }

    @Test
    @DisplayName("대시보드 수정")
    public void updateDashboardTest() throws Exception {
        //given
        Dashboard dashboard = getDashboard(order, "dashboard1");
        DashboardDto dashboardDto = new DashboardDto();
        dashboardDto.setTitle("dashboard2");
        dashboardDto.setContent("content2");
        given(dashboardRepository.getOne(dashboardDto.getId()))
                .willReturn(dashboard);

        //when
        dashboardService.updateDashboard(dashboardDto);

        //then
        then("dashboard2").isEqualTo(dashboard.getTitle());
        then("content2").isEqualTo(dashboard.getContent());
    }

    @Test
    @DisplayName("대시보드 삭제")
    public void deleteDashboardTest() throws Exception {
        //given
        Dashboard dashboard = getDashboard(order, "dashboard");

        //when
        dashboardService.deleteDashboard(dashboard.getId());

        //then
        verify(dashboardRepository).deleteById(dashboard.getId());
    }

    private Dashboard getDashboard(Order order, String title) {
        return Dashboard.createDashboard(
                order, title, "content"
        );
    }

}