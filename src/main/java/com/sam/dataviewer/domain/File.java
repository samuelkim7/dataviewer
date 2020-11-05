package com.sam.dataviewer.domain;

import com.sam.dataviewer.dto.FileDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File {

    @Id
    @Column(name = "file_id")
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* 연관관계 메서드 */
    public void setOrder(Order order) {
        this.order = order;
        order.getFiles().add(this);
    }

    /* 생성 메서드 */
    public static File createFile(
            Order order, String originalFileName,
            String fileName, String filePath, Long fileSize
    ) {
        File file = new File();
        file.setOrder(order);
        file.originalFileName = originalFileName;
        file.fileName = fileName;
        file.filePath = filePath;
        file.fileSize = fileSize;
        return file;
    }

    /* dto Object로 변환 */
    public FileDto toDto() {
        FileDto dto = new FileDto();
        dto.setId(this.getId());
        dto.setOriginalFileName(this.getOriginalFileName());
        dto.setFileName(this.getFileName());
        dto.setFileSize(this.getFileSize());
        return dto;
    }
}
