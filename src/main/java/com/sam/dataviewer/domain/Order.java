package com.sam.dataviewer.domain;

import com.sam.dataviewer.dto.OrderDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    private String content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;   // WAIT, ORDER, COMPLETE, CANCEL

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Estimate> estimates = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Dashboard> dashboards = new ArrayList<>();

    /* 생성 메서드 */
    public static Order createOrder(
            Member member, String title,
            String content
    ) {
        Order order = new Order();
        order.setMember(member);
        order.title = title;
        order.content = content;
        order.status = OrderStatus.WAIT;
        return order;
    }

    /* 연관관계 메서드 */
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    /* 의뢰 취소 */
    public void cancel() {
        this.status = OrderStatus.CANCEL;
    }

    /* dto Object로 변환 */
    public OrderDto toDto() {
        OrderDto dto = new OrderDto();
        dto.setId(this.getId());
        dto.setTitle(this.getTitle());
        dto.setContent(this.getContent());
        dto.setCreatedAt(this.getCreatedAt());
        dto.setStatus(this.getStatus());
        return dto;
    }

    /* 의뢰 수정 */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    /* 의뢰 시작 */
    public void start() {
        this.status = OrderStatus.ORDER;
    }
}
