package com.sam.dataviewer.domain;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class Figure {

    @Id
    @Column(name = "figure_id")
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "dashboard_id")
    private Dashboard dashboard;

    @Enumerated(EnumType.STRING)
    private FigureType type;

    private String title;

    private String description;

    private String image;
}
