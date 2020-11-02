package com.sam.dataviewer.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter @Setter
public class DashboardDto {

    private Long id;

    @NotEmpty(message = "제목을 기입해주세요.")
    private String title;

    @NotEmpty(message = "내용을 기입해주세요.")
    private String content;

    private LocalDateTime createdAt;
}
