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

    private String iframeTag;

    private Long dashboardId;

    private String dashboardTitle;

    private LocalDateTime createdAt;

    public FigureDto() {
    }

    public FigureDto(
            Long id, @NotEmpty(message = "제목을 기입해주세요.") String title,
            @NotEmpty(message = "설명을 기입해주세요.") String description,
            String originalFileName, String fileName, String iframeTag,
            Long dashboardId, String dashboardTitle, LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.originalFileName = originalFileName;
        this.fileName = fileName;
        this.iframeTag = iframeTag;
        this.dashboardId = dashboardId;
        this.dashboardTitle = dashboardTitle;
        this.createdAt = createdAt;
    }
}
