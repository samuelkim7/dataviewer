package com.sam.dataviewer.repository;

import com.sam.dataviewer.domain.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {
}
