package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Dashboard;
import com.sam.dataviewer.domain.Figure;
import com.sam.dataviewer.dto.FigureDto;
import com.sam.dataviewer.repository.DashboardRepository;
import com.sam.dataviewer.repository.FigureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FigureService {

    private final FigureRepository figureRepository;
    private final DashboardRepository dashboardRepository;

    @Transactional
    public Long create(Long dashboardId, FigureDto dto, String originalFileName) {
        Dashboard dashboard = dashboardRepository.getOne(dashboardId);
        Figure figure = Figure.createFigure(
                dashboard, dto.getTitle(),
                dto.getDescription(), originalFileName
        );

        figureRepository.save(figure);
        return figure.getId();
    }

    /* Figure 전체 조회 for ADMIN */
    public List<FigureDto> findAll() {
        List<Figure> figures = figureRepository.findGroupByDashboard();
        List<FigureDto> figureDtos = new ArrayList<>();
        for (Figure figure : figures) {
            figureDtos.add(figure.toDto());
        }
        return figureDtos;
    }
}
