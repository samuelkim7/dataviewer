package com.sam.dataviewer.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Dashboard {

    @Id
    @Column(name = "dashboard_id")
    @GeneratedValue
    private Long id;

    private String title;

    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "dashboard")
    private List<Figure> figures = new ArrayList<>();
}
