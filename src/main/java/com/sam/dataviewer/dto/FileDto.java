package com.sam.dataviewer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FileDto {

    private Long id;

    private String originalFileName;

    private String fileName;

    private Long fileSize;

    public FileDto() {
    }

    public FileDto(
            Long id, String originalFileName, String fileName, Long fileSize
    ) {
        this.id = id;
        this.originalFileName = originalFileName;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }
}
