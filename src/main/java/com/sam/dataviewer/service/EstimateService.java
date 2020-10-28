package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Estimate;
import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.dto.EstimateDto;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.repository.EstimateRepository;
import com.sam.dataviewer.repository.MemberRepository;
import com.sam.dataviewer.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EstimateService {

    private final EstimateRepository estimateRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

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

    /* 견적 전체 조회 for ADMIN */
    public List<EstimateDto> findAll() {
        List<Estimate> estimates = estimateRepository.findAll();
        List<EstimateDto> estimateDtos = new ArrayList<>();
        for (Estimate estimate : estimates) {
            estimateDtos.add(estimate.toDto());
        }
        return estimateDtos;
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
        estimate.update(dto.getTitle(), dto.getContent(), dto.getPrice());
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
    public List<EstimateDto> findEstimatesByUsername(String username) {
        Member member = memberRepository.findByUsername(username);
        List<Estimate> estimates = estimateRepository.findByMemberOrderByIdDesc(member);
        List<EstimateDto> estimateDtos = new ArrayList<>();
        for (Estimate estimate : estimates) {
            estimateDtos.add(estimate.toDto());
        }
        return estimateDtos;
    }
}
