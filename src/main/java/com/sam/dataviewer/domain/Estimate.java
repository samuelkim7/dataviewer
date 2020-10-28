package com.sam.dataviewer.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Estimate {

    @Id
    @Column(name = "estimate_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private String title;

    private String content;

    private Long price;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private EstimateStatus status;

    /* 연관관계 메서드 */
    public void setOrder(Order order) {
        this.order = order;
        order.getEstimates().add(this);
    }

    /* 생성 메서드 */
    public static Estimate createEstimate() {
    }
}
