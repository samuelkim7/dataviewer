package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.EstimateStatus;
import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.dto.EstimateDto;
import com.sam.dataviewer.dto.MemberDto;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.repository.EstimateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class EstimateServiceTest {

    @Autowired EstimateService estimateService;
    @Autowired OrderService orderService;
    @Autowired MemberService memberService;
    @Autowired EstimateRepository estimateRepository;

    @Test
    public void 견적_신청() throws Exception {
        //given
        Member member = getMember();
        Long orderId = getOrder(member);

        EstimateDto estimateDto = new EstimateDto();
        estimateDto.setTitle("새 견적");

        //when
        Long estimateId = estimateService.request(orderId, estimateDto);

        //then
        assertThat(estimateDto.getTitle())
                .isEqualTo(estimateRepository.getOne(estimateId).getTitle());
    }

    @Test
    public void 회원_아이디별_견적_전체_조회() throws Exception {
        //given
        Member member = getMember();
        Long orderId = getOrder(member);

        EstimateDto estimateDto1 = new EstimateDto();
        estimateDto1.setTitle("견적1");
        estimateService.request(orderId, estimateDto1);

        EstimateDto estimateDto2 = new EstimateDto();
        estimateDto2.setTitle("견적2");
        estimateService.request(orderId, estimateDto2);

        //when
        List<EstimateDto> estimateDtos = estimateService.findByUsername(member.getUsername());

        //then
        assertThat(2).isEqualTo(estimateDtos.size());
        assertThat(estimateDto2.getTitle())
                .isEqualTo(estimateDtos.get(0).getTitle());
        assertThat(estimateDto1.getTitle())
                .isEqualTo(estimateDtos.get(1).getTitle());
    }

    @Test
    public void 견적_취소() throws Exception {
        //given
        Member member = getMember();
        Long orderId = getOrder(member);

        EstimateDto estimateDto = new EstimateDto();
        estimateDto.setTitle("새 견적");
        Long estimateId = estimateService.request(orderId, estimateDto);

        //when
        estimateService.cancelEstimate(estimateId);

        //then
        assertThat(EstimateStatus.CANCEL)
                .isEqualTo(estimateRepository.getOne(estimateId).getStatus());
    }

    @Test
    public void 견적_수정() throws Exception {
        //given
        Member member = getMember();
        Long orderId = getOrder(member);

        EstimateDto estimateDto1 = new EstimateDto();
        estimateDto1.setTitle("새 견적");
        Long estimateId = estimateService.request(orderId, estimateDto1);

        EstimateDto estimateDto2 = new EstimateDto();
        estimateDto2.setId(estimateId);
        estimateDto2.setTitle("수정된 견적");

        //when
        estimateService.updateEstimate(estimateDto2);

        //then
        assertThat(estimateDto2.getTitle())
                .isEqualTo(estimateRepository.getOne(estimateId).getTitle());
    }

    @Test
    public void 견적_승낙() throws Exception {
        //given
        Member member = getMember();
        Long orderId = getOrder(member);

        EstimateDto estimateDto = new EstimateDto();
        estimateDto.setTitle("새 견적");
        Long estimateId = estimateService.request(orderId, estimateDto);

        //when
        estimateService.acceptEstimate(estimateId);

        //then
        assertThat(EstimateStatus.ACCEPT)
                .isEqualTo(estimateRepository.getOne(estimateId).getStatus());
    }

    private Member getMember() {
        MemberDto memberDto = new MemberDto();
        memberDto.setUsername("kim");
        memberDto.setPassword("1234");
        return memberService.join(memberDto);
    }

    private Long getOrder(Member member) {
        OrderDto orderDto = new OrderDto();
        orderDto.setTitle("새 의뢰");
        return orderService.order(member.getUsername(), orderDto);
    }
}