package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.File;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.dto.FileDto;
import com.sam.dataviewer.exception.CustomException;
import com.sam.dataviewer.repository.FileRepository;
import com.sam.dataviewer.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final OrderRepository orderRepository;

    /* file 업로드 */
    public String uploadFile(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String originalFileName = file.getOriginalFilename();
            String fileName = getFileName(originalFileName);
            Path path = Paths.get("C:/spring/dataviewer_files/" + fileName);
            Files.write(path, bytes);
            return fileName;
        } catch(IOException e) {
            throw new CustomException("파일 업로드가 실패했습니다. 관리자에게 문의하세요.");
        }
    }

    /* file 업로드 및 info 저장 */
    @Transactional
    public void saveFile(Long orderId, List<MultipartFile> files) {
        for (MultipartFile file : files) {
            try {
                // 파일 저장
                byte[] bytes = file.getBytes();
                String originalFileName = file.getOriginalFilename();
                String fileName = getFileName(originalFileName);
                Path path = Paths.get("C:/spring/dataviewer_files/" + fileName);
                Files.write(path, bytes);

                //파일 정보 DB에 저장
                Order order = orderRepository.findById(orderId).orElse(null);
                File fileDB = File.createFile(
                        order, originalFileName, fileName,
                        path.toString(), file.getSize()
                );
                fileRepository.save(fileDB);
            } catch(IOException e) {
                throw new CustomException("파일 업로드가 실패했습니다. 관리자에게 문의하세요.");
            }
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
        Order order = orderRepository.findById(id).orElse(null);
        List<File> files = fileRepository.findByOrderOrderByIdDesc(order);
        return files.stream().map(f -> f.toDto()).collect(Collectors.toList());
    }

    /* file 삭제 */
    public void deleteFile(String fileName) {
        if (fileName != null) {
            Path path = Paths.get("C:/spring/dataviewer_files/" + fileName);
            try {
                Files.delete(path);
            } catch(IOException e) {
                throw new CustomException("파일 삭제가 실패했습니다. 관리자에게 문의하세요.");
            }
        }
    }

    /* file 삭제 및 info 삭제 */
    @Transactional
    public void delete(Long id) {
        File file = fileRepository.findById(id).orElse(null);
        if (file != null) {
            Path path = Paths.get(file.getFilePath());
            try {
                Files.delete(path);
            } catch(IOException e) {
                throw new CustomException("파일 삭제가 실패했습니다. 관리자에게 문의하세요.");
            }
            fileRepository.delete(file);
        }
    }

    /* 파일 다운로드 */
    public Resource downloadFile(String fileName) {
        Path path = Paths.get("C:/spring/dataviewer_files/" + fileName);
        try {
            InputStreamResource resource = new InputStreamResource(Files.newInputStream(path));
            return resource;
        } catch(IOException e) {
            throw new CustomException("파일 다운로드가 실패했습니다. 관리자에게 문의하세요.");
        }
    }
}
