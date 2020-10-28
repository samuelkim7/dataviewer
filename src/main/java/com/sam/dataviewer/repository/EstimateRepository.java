package com.sam.dataviewer.repository;

import com.sam.dataviewer.domain.Estimate;
import com.sam.dataviewer.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {
    List<Estimate> findByMemberOrderByIdDesc(Member member);
}
