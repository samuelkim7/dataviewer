package com.sam.dataviewer.domain;

import com.sam.dataviewer.dto.OrderDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter
@DynamicUpdate   // Dirty Checking 시 변경된 field만 update
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "dashboard_id")
    private Dashboard dashBoard;

    private String title;

    private String content;

    private String file;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;   // WAIT, ORDER, CANCEL

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Estimate> estimates = new ArrayList<>();

    /* 연관관계 메서드 */
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    /* 생성 메서드 */
    public static Order createOrder(
            Member member, String title,
            String content, String file
    ) {
        Order order = new Order();
        order.setMember(member);
        order.title = title;
        order.content = content;
        order.file = file;
        order.createdAt = LocalDateTime.now();
        order.status = OrderStatus.WAIT;
        return order;
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
        dto.setFile(this.getFile());
        dto.setStatus(this.getStatus());
        return dto;
    }

    /* 의뢰 수정 */
    // dto Object에서 가져온 정보 사용. Dirty Checking을 통한 수정
    public void update(String title, String content, String file) {
        this.title = title;
        this.content = content;
        this.file = file;
    }

    /* 의뢰 시작 */
    public void start() {
        this.status = OrderStatus.ORDER;
    }
}
