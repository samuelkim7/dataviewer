package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Estimate;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.dto.EstimateDto;
import com.sam.dataviewer.repository.EstimateRepository;
import com.sam.dataviewer.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
                order, dto.getTitle(), dto.getContent(),
                dto.getPrice(), dto.getDuration()
        );

        estimateRepository.save(estimate);
        return estimate.getId();
    }

    /* 견적 전체 조회 for ADMIN */
    public List<EstimateDto> findAll() {
        List<Estimate> estimates = estimateRepository.findByOrderByIdDesc();
        return estimates.stream().map(e -> e.toDto()).collect(Collectors.toList());
    }

    /* 견적 한 건 조회 */
    public EstimateDto findOne(Long id) {
        Estimate estimate = estimateRepository.getOne(id);
        return estimate.toDto();
    }

    /* 견적 수정 */
    @Transactional
    public void updateEstimate(EstimateDto dto) {
        Estimate estimate = estimateRepository.getOne(dto.getId());
        estimate.update(dto.getTitle(), dto.getContent(), dto.getPrice(), dto.getDuration());
    }

    /* 견적 취소 */
    @Transactional
    public void cancelEstimate(Long id) {
        Estimate estimate = estimateRepository.getOne(id);
        estimate.cancel();
    }

    /* 견적 승낙 */
    @Transactional
    public void acceptEstimate(Long id) {
        Estimate estimate = estimateRepository.getOne(id);
        estimate.accept();
    }

    /* 회원 아이디별 견적 전체 조회 for USER */
    public List<EstimateDto> findByUsername(String username) {
        List<Estimate> estimates = estimateRepository.findByUsername(username);
        return estimates.stream().map(e -> e.toDto()).collect(Collectors.toList());
    }

}
