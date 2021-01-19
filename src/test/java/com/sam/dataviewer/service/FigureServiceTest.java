package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Dashboard;
import com.sam.dataviewer.domain.Figure;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.dto.FigureDto;
import com.sam.dataviewer.repository.DashboardRepository;
import com.sam.dataviewer.repository.FigureRepository;
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
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FigureServiceTest {

    @InjectMocks
    private FigureService figureService;
    @Mock
    private FigureRepository figureRepository;
    @Mock
    private DashboardRepository dashboardRepository;
    @Mock
    private Order order;
    @Captor
    private ArgumentCaptor<Figure> argumentCaptor;

    @Test
    @DisplayName("figure 생성")
    public void createTest() throws Exception {
        //given
        Dashboard dashboard = getDashboard(order);
        Optional<Dashboard> optional = Optional.of(dashboard);
        given(dashboardRepository.findById(dashboard.getId()))
                .willReturn(optional);
        FigureDto figureDto = new FigureDto();
        figureDto.setTitle("figure");

        //when
        figureService.create(
                dashboard.getId(), figureDto, "file", null
        );

        //then
        verify(figureRepository, times(1)).save(argumentCaptor.capture());
        then(argumentCaptor.getValue()).isNotNull();
        then(argumentCaptor.getValue().getTitle()).isEqualTo("figure");
        then(argumentCaptor.getValue().getOriginalFileName()).isEqualTo("file");
    }

    @Test
    @DisplayName("대시보드로 조회")
    public void findByDashboard() throws Exception {
        //given
        Dashboard dashboard = getDashboard(order);
        Figure figure1 = getFigure(dashboard, "figure1");
        Figure figure2 = getFigure(dashboard, "figure2");
        List<Figure> figures = new ArrayList<>();
        figures.add(figure1);
        figures.add(figure2);
        given(figureRepository.findByDashboardId(dashboard.getId()))
                .willReturn(figures);

        //when
        List<FigureDto> figureDtos = figureService.findByDashboard(dashboard.getId());

        //then
        then(figureDtos.size()).isEqualTo(2);
        then(figureDtos.get(0).getDashboardTitle()).isEqualTo("dashboard");
        then(figureDtos.get(0).getTitle()).isEqualTo("figure1");
        then(figureDtos.get(1).getTitle()).isEqualTo("figure2");
    }

    @Test
    @DisplayName("figure 수정")
    public void updateFigureTest() throws Exception {
        //given
        Dashboard dashboard = getDashboard(order);
        Figure figure = getFigure(dashboard, "figure1");
        FigureDto figureDto = new FigureDto();
        figureDto.setTitle("figure2");
        figureDto.setDescription("description2");
        Optional<Figure> optional = Optional.of(figure);
        given(figureRepository.findById(figureDto.getId()))
                .willReturn(optional);

        //when
        figureService.updateFigure(
                figureDto, null, null
        );

        //then
        then(figure.getTitle()).isEqualTo("figure2");
        then(figure.getDescription()).isEqualTo("description2");
    }

    @Test
    public void deleteFigureTest() throws Exception {
        //given
        Dashboard dashboard = getDashboard(order);
        Figure figure = getFigure(dashboard, "figure");

        //when
        figureService.deleteFigure(figure.getId());

        //then
        verify(figureRepository).deleteById(figure.getId());
    }

    private Figure getFigure(Dashboard dashboard, String title) {
        return Figure.createFigure(
                dashboard, title, "description",
                null, null
        );
    }

    private Dashboard getDashboard(Order order) {
        return Dashboard.createDashboard(
                order, "dashboard", "content"
        );
    }
}