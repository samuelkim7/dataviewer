package com.sam.dataviewer.dto;

import com.sam.dataviewer.domain.EstimateStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter @Setter
public class EstimateDto {

    private Long id;

    @NotEmpty(message = "제목을 기입해주세요.")
    private String title;

    @NotEmpty(message = "견적 사항을 기입해주세요.")
    private String content;

    @NotEmpty(message = "견적 금액을 기입해주세요.")
    private Long price;

    @NotEmpty(message = "예상 소요 일자를 기입해주세요.")
    private int duration;

    private LocalDateTime createdAt;

    private EstimateStatus status;

}
