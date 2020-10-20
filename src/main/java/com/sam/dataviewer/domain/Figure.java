package com.sam.dataviewer.domain;

import javax.persistence.*;

public class Figure {

    @Id
    @Column(name = "figure_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FigureType type;

    private String title;

    private String description;

    private String image;
}
