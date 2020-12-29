package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Estimate;
import com.sam.dataviewer.domain.EstimateStatus;
import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.dto.EstimateDto;
import com.sam.dataviewer.repository.EstimateRepository;
import com.sam.dataviewer.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class EstimateServiceTest {

    @InjectMocks
    private EstimateService estimateService;
    @Mock
    private OrderService orderService;
    @Mock
    private EstimateRepository estimateRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private Member member;
    @Captor
    private ArgumentCaptor<Estimate> argumentCaptor;

    @Test
    @DisplayName("견적 요청")
    public void requestTest() throws Exception {
        //given
        Order order = getOrder();
        EstimateDto estimateDto = new EstimateDto();
        estimateDto.setTitle("estimate");
        estimateDto.setPrice(100000L);
        Optional<Order> optional = Optional.of(order);
        given(orderRepository.findById(order.getId())).willReturn(optional);

        //when
        estimateService.request(order.getId(), estimateDto);

        //then
        verify(estimateRepository, times(1)).save(argumentCaptor.capture());
        then(argumentCaptor.getValue()).isNotNull();
        then(argumentCaptor.getValue().getTitle()).isEqualTo("estimate");
        then(argumentCaptor.getValue().getPrice()).isEqualTo(100000L);
    }

    @Test
    @DisplayName("회원 아이디로 조회")
    public void findByUsernameTest() throws Exception {
        //given
        Order order = getOrder();
        Estimate estimate1 = getEstimate(order, "estimate1");
        Estimate estimate2 = getEstimate(order, "estimate2");
        List<Estimate> estimates = new ArrayList<>();
        estimates.add(estimate1);
        estimates.add(estimate2);
        given(estimateRepository.findByUsername(member.getUsername()))
                .willReturn(estimates);

        //when
        List<EstimateDto> estimateDtos = estimateService.findByUsername(member.getUsername());

        //then
        then(estimateDtos.size()).isEqualTo(2);
        then(estimateDtos.get(0).getTitle()).isEqualTo("estimate1");
        then(estimateDtos.get(1).getTitle()).isEqualTo("estimate2");
    }

    @Test
    @DisplayName("견적 수정")
    public void updateEstimateTest() throws Exception {
        //given
        Order order = getOrder();
        Estimate estimate = getEstimate(order, "estimate1");

        EstimateDto estimateDto = new EstimateDto();
        estimateDto.setTitle("estimate2");
        Optional<Estimate> optional = Optional.of(estimate);
        given(estimateRepository.findById(estimate.getId())).willReturn(optional);

        //when
        estimateService.updateEstimate(estimateDto);

        //then
        then(estimate.getTitle()).isEqualTo("estimate2");
    }

    @Test
    @DisplayName("견적 취소")
    public void cancelEstimateTest() throws Exception {
        //given
        Order order = getOrder();
        Estimate estimate = getEstimate(order, "estimate");
        Optional<Estimate> optional = Optional.of(estimate);
        given(estimateRepository.findById(estimate.getId())).willReturn(optional);

        //when
        estimateService.cancelEstimate(estimate.getId());

        //then
        then(estimate.getStatus()).isEqualTo(EstimateStatus.CANCEL);
    }

    @Test
    @DisplayName("견적 승낙")
    public void acceptEstimateTest() throws Exception {
        //given
        Order order = getOrder();
        Estimate estimate = getEstimate(order, "estimate");
        Optional<Estimate> optional = Optional.of(estimate);
        given(estimateRepository.findById(estimate.getId())).willReturn(optional);

        //when
        estimateService.acceptEstimate(estimate.getId());

        //then
        then(estimate.getStatus()).isEqualTo(EstimateStatus.ACCEPT);
    }

    private Order getOrder() {
        return Order.createOrder(
                member, "order", "content"
        );
    }

    private Estimate getEstimate(Order order, String title) {
        return Estimate.createEstimate(
                order, title, null, null, null
                );
    }

}