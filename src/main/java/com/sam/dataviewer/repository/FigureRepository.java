package com.sam.dataviewer.repository;

import com.sam.dataviewer.domain.Figure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FigureRepository extends JpaRepository<Figure, Long> {
}
