package com.sam.dataviewer.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter @Setter
public class FigureDto {

    private Long id;

    @NotEmpty(message = "제목을 기입해주세요.")
    private String title;

    @NotEmpty(message = "설명을 기입해주세요.")
    private String description;

    private String originalFileName;

    private String fileName;

    private String dashboardTitle;

    private LocalDateTime createdAt;
}
