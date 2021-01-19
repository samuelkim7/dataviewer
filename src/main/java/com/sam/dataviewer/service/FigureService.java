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
import java.util.Optional;
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
        Dashboard dashboard = dashboardRepository.findById(dashboardId).orElse(null);
        Figure figure = Figure.createFigure(
                dashboard, dto.getTitle(), dto.getDescription(),
                originalFileName, fileName
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
        Optional<Figure> optional = figureRepository.findById(id);
        return optional.map(f -> f.toDto()).orElse(new FigureDto());
    }

    /* Figure 수정 */
    @Transactional
    public void updateFigure(FigureDto dto, String originalFilename, String fileName) {
        Figure figure = figureRepository.findById(dto.getId()).orElse(null);
        if (figure != null) {
            figure.update(
                    dto.getTitle(), dto.getDescription(),
                    originalFilename, fileName);
        }
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
