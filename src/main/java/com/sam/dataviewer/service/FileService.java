package com.sam.dataviewer.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileService {

    /* 분석 file 저장 */
    public String saveFile(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String originalFileName = file.getOriginalFilename();
        String fileName = getFileName(originalFileName);
        Path path = Paths.get("src/main/resources/static/file/" + fileName);
        Files.write(path, bytes);
        return fileName;
    }

    /* 현재 시각 덧붙여서 fileName 생성 */
    private String getFileName(String originalFileName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("(yyyy-MM-dd HH시mm분ss초)");
        String fileName = originalFileName + LocalDateTime.now().format(formatter);
        return fileName;
    }

    /* 분석 file 수정 */
    public String updateFile(String currentFileName, MultipartFile newFile) throws IOException {
        //현 파일 삭제
        if (currentFileName != null) {
            Path pathCurrent = Paths.get("src/main/resources/static/file/" + currentFileName);
            Files.delete(pathCurrent);
        }

        //새 파일 저장
        byte[] bytes = newFile.getBytes();
        String originalFileName = newFile.getOriginalFilename();
        String fileName = getFileName(originalFileName);
        Path pathNew = Paths.get("src/main/resources/static/file/" + fileName);
        Files.write(pathNew, bytes);
        return fileName;
    }
}
