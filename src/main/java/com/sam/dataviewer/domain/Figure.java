package com.sam.dataviewer.domain;

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

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* 생성 메서드 */
    public static Figure createFigure(
            Dashboard dashboard, String title,
            String description, String originalFileName
    ) {
        Figure figure = new Figure();
        figure.setDashboard(dashboard);
        figure.title = title;
        figure.description = description;
        figure.originalFileName = originalFileName;
        return figure;
    }

    /* 연관관계 메서드 */
    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
        dashboard.getFigures().add(this);
    }
}
