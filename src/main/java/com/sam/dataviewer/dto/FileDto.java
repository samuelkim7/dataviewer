package com.sam.dataviewer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FileDto {

    private String originalFileName;

    private String fileName;

    private Long fileSize;
}
