package com.sam.dataviewer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FileDto {

    private Long id;

    private String originalFileName;

    private String fileName;

    private Long fileSize;
}
