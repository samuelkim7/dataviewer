package com.sam.dataviewer.domain;

import com.sam.dataviewer.dto.DashboardDto;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class Dashboard {

    @Id
    @Column(name = "dashboard_id")
    @GeneratedValue
    private Long id;

    private String title;

    private String content;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "dashboard")
    private List<Figure> figures = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    /* 생성 메서드 */
    public static Dashboard createDashboard(
            Order order, String title, String content
    ) {
        Dashboard dashboard = new Dashboard();
        dashboard.setOrder(order);
        dashboard.title = title;
        dashboard.content = content;
        return dashboard;
    }

    /* 연관관계 메서드 */
    public void setOrder(Order order) {
        this.order = order;
        order.setDashBoard(this);
    }

    /* dto Object로 변환 */
    public DashboardDto toDto() {
        DashboardDto dto = new DashboardDto();
        dto.setId(this.getId());
        dto.setTitle(this.getTitle());
        dto.setContent(this.getContent());
        dto.setCreatedAt(this.getCreatedAt());
        dto.setOrderTitle(this.getOrder().getTitle());
        return dto;
    }

    /* Dashboard 수정 */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
