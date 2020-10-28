package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Estimate;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.dto.EstimateDto;
import com.sam.dataviewer.repository.EstimateRepository;
import com.sam.dataviewer.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EstimateService {

    private final EstimateRepository estimateRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Long request(Long orderId, EstimateDto dto) {
        Order order = orderRepository.getOne(orderId);

        Estimate estimate = Estimate.createEstimate(
                order, dto.getTitle(),
                dto.getContent(), dto.getPrice()
        );

        estimateRepository.save(estimate);
        return estimate.getId();
    }
}
