package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Dashboard;
import com.sam.dataviewer.domain.Figure;
import com.sam.dataviewer.dto.FigureDto;
import com.sam.dataviewer.repository.DashboardRepository;
import com.sam.dataviewer.repository.FigureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FigureService {

    private final FigureRepository figureRepository;
    private final DashboardRepository dashboardRepository;

    @Transactional
    public Long create(Long dashboardId, FigureDto dto,
                       String originalFileName, String fileName) {
        Dashboard dashboard = dashboardRepository.getOne(dashboardId);
        Figure figure = Figure.createFigure(
                dashboard, dto.getTitle(),
                dto.getDescription(), originalFileName,
                fileName, dto.getIframeTag()
        );

        figureRepository.save(figure);
        return figure.getId();
    }

    /* Figure 전체 조회 for ADMIN */
    public List<FigureDto> findAll() {
        List<Figure> figures = figureRepository.findOrderByDashboard();
        return figures.stream().map(f -> f.toDto()).collect(Collectors.toList());
    }

    /* Figure 한건 조회 */
    public FigureDto findOne(Long id) {
        Figure figure = figureRepository.getOne(id);
        return figure.toDto();
    }

    /* Figure 수정 */
    @Transactional
    public void update(FigureDto dto, String originalFilename, String fileName) {
        Figure figure = figureRepository.getOne(dto.getId());
        figure.update(
                dto.getTitle(), dto.getDescription(),
                originalFilename, fileName, dto.getIframeTag());
    }

    /* Figure 삭제 */
    @Transactional
    public void deleteFigure(Long id) {
        figureRepository.deleteById(id);
    }

    /* Dashboard에 해당하는 figure 조회 */
    public List<FigureDto> findByDashboard(Long dashboardId) {
        List<Figure> figures = figureRepository.findByDashboardId(dashboardId);
        return figures.stream().map(f -> f.toDto()).collect(Collectors.toList());
    }
}
