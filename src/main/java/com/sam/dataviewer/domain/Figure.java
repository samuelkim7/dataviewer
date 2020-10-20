package com.sam.dataviewer.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

public class Figure {

    @Id
    @Column(name = "figure_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
