package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.File;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.dto.FileDto;
import com.sam.dataviewer.repository.FileRepository;
import com.sam.dataviewer.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final OrderRepository orderRepository;

    /* 분석 file 업로드 */
    public void uploadFile(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String originalFileName = file.getOriginalFilename();
        String fileName = getFileName(originalFileName);
        Path path = Paths.get("src/main/resources/static/file/" + fileName);
        Files.write(path, bytes);
    }

    /* 분석 file 업로드 및 info 저장 */
    @Transactional
    public void saveFile(Long orderId, List<MultipartFile> files) throws IOException {
        for (MultipartFile file : files) {
            // 파일 저장
            byte[] bytes = file.getBytes();
            String originalFileName = file.getOriginalFilename();
            String fileName = getFileName(originalFileName);
            Path path = Paths.get("src/main/resources/static/file/" + fileName);
            Files.write(path, bytes);

            //파일 정보 DB에 저장
            Order order = orderRepository.getOne(orderId);
            File fileDB = File.createFile(
                    order, originalFileName, fileName,
                    path.toString(), file.getSize()
            );
            fileRepository.save(fileDB);
        }
    }

    /* fileName 랜덤 생성 */
    private String getFileName(String originalFileName) {
        //uuid (Universal Unique IDentifier) 사용
        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString() + "_" + originalFileName;
        return fileName;
    }

    /* 의뢰 id로 file 전체 조회  */
    public List<FileDto> findByOrderId(Long id) {
        Order order = orderRepository.getOne(id);
        List<File> files = fileRepository.findByOrderOrderByIdDesc(order);
        List<FileDto> fileDtos = new ArrayList<>();
        for (File file : files) {
            fileDtos.add(file.toDto());
        }
        return fileDtos;
    }

    /* file 삭제 */
    public void deleteFile(Long id) throws IOException {
        File file = fileRepository.getOne(id);
        Path path = Paths.get(file.getFilePath());
        Files.delete(path);
    }

    /* file 삭제 및 info 삭제 */
    @Transactional
    public void delete(Long id) throws IOException {
        File file = fileRepository.getOne(id);
        Path path = Paths.get(file.getFilePath());
        Files.delete(path);

        fileRepository.delete(file);
    }

    /* 파일 다운로드 */
    public Resource downloadFile(String originalFileName) throws IOException {
        File file = fileRepository.findByOriginalFileName(originalFileName);
        Path path = Paths.get(file.getFilePath());
        InputStreamResource resource = new InputStreamResource(Files.newInputStream(path));
        return resource;
    }
}
