package com.sam.dataviewer.repository;

import com.sam.dataviewer.domain.Figure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FigureRepository extends JpaRepository<Figure, Long> {

    @Query("SELECT f FROM Figure as f" +
            " join f.dashboard as d" +
            " ORDER BY d.id DESC")
    List<Figure> findOrderByDashboard();

    @Query("SELECT f FROM Figure as f" +
            " join f.dashboard as d" +
            " WHERE d.id = :id")
    List<Figure> findByDashboardId(@Param("id") Long dashboardId);
}
