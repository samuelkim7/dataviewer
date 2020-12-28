package com.sam.dataviewer.dto;

import com.sam.dataviewer.domain.EstimateStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
public class EstimateDto {

    private Long id;

    @NotEmpty(message = "제목을 기입해주세요.")
    private String title;

    @NotEmpty(message = "견적 사항을 기입해주세요.")
    private String content;

    @NotNull(message = "견적 금액을 기입해주세요.")
    private Long price;

    @NotEmpty(message = "예상 소요 일자를 기입해주세요.")
    private String duration;

    private LocalDateTime createdAt;

    private EstimateStatus status;

    public EstimateDto() {
    }

    public EstimateDto(
            Long id, @NotEmpty(message = "제목을 기입해주세요.") String title,
            @NotEmpty(message = "견적 사항을 기입해주세요.") String content,
            @NotNull(message = "견적 금액을 기입해주세요.") Long price,
            @NotEmpty(message = "예상 소요 일자를 기입해주세요.") String duration,
            LocalDateTime createdAt, EstimateStatus status
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.price = price;
        this.duration = duration;
        this.createdAt = createdAt;
        this.status = status;
    }
}
