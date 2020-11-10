package com.sam.dataviewer.domain;

import com.sam.dataviewer.dto.FigureDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Figure {

    @Id
    @Column(name = "figure_id")
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "dashboard_id")
    private Dashboard dashboard;

    private String title;

    private String description;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "file_name")
    private String fileName;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* 생성 메서드 */
    public static Figure createFigure(
            Dashboard dashboard, String title,
            String description, String originalFileName,
            String fileName
    ) {
        Figure figure = new Figure();
        figure.setDashboard(dashboard);
        figure.title = title;
        figure.description = description;
        figure.originalFileName = originalFileName;
        figure.fileName = fileName;
        return figure;
    }

    /* 연관관계 메서드 */
    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
        dashboard.getFigures().add(this);
    }

    /* dto Object로 변환 */
    public FigureDto toDto() {
        FigureDto dto = new FigureDto();
        dto.setId(this.getId());
        dto.setTitle(this.getTitle());
        dto.setDescription(this.getDescription());
        dto.setOriginalFileName(this.getOriginalFileName());
        dto.setFileName(this.getFileName());
        dto.setDashboardTitle(this.getDashboard().getTitle());
        dto.setCreatedAt(this.getCreatedAt());
        return dto;
    }

    /* Figure 수정 */
    public void update(
            String title, String description,
            String originalFilename, String fileName
    ) {
        this.title = title;
        this.description = description;
        this.originalFileName = originalFilename;
        this.fileName = fileName;
    }
}
