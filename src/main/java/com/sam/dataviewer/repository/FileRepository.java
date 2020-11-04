package com.sam.dataviewer.repository;

import com.sam.dataviewer.domain.File;
import com.sam.dataviewer.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByOrderOrderByIdDesc(Order order);

    File findByFileName(String fileName);
}
