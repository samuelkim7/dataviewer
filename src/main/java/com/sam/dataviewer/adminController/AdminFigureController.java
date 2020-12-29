package com.sam.dataviewer.adminController;

import com.sam.dataviewer.dto.DashboardDto;
import com.sam.dataviewer.dto.FigureDto;
import com.sam.dataviewer.service.DashboardService;
import com.sam.dataviewer.service.FigureService;
import com.sam.dataviewer.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminFigureController {

    private final FigureService figureService;
    private final DashboardService dashboardService;
    private final FileService fileService;

    @GetMapping("/figure/new")
    public String createForm(Model model) {
        List<DashboardDto> dashboardDtos = dashboardService.findAll();
        model.addAttribute("dashboardDtos", dashboardDtos);
        model.addAttribute("figureDto", new FigureDto());
        return "admin/figure/createFigureForm";
    }

    @PostMapping("/figure/new")
    public String createFigure(
            @RequestParam("dashboardId") Long dashboardId,
            @Valid FigureDto figureDto,
            MultipartFile file,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return "admin/figure/createFigureForm";
        }

        //파일 업로드
        String fileName = null;
        try {
            if (file != null && !file.isEmpty()) {
                fileName = fileService.uploadFile(file);
            }
        } catch (IOException e) {
            //
        }

        //figure Entity 생성 및 저장
        if (file != null && !file.isEmpty()) {
            figureService.create(dashboardId, figureDto, file.getOriginalFilename(), fileName);
        } else {
            figureService.create(dashboardId, figureDto, null, null);
        }

        return "redirect:/admin/figures";
    }

    @GetMapping("/figures")
    public String figureList(Model model) {
        List<FigureDto> figureDtos = figureService.findAll();
        model.addAttribute("figureDtos", figureDtos);
        return "admin/figure/figureList";
    }

    @GetMapping("/figure/figureDetail/{id}")
    public String figureDetail(@PathVariable Long id, Model model) {
        FigureDto figureDto = figureService.findOne(id);
        model.addAttribute("figureDto", figureDto);
        return "admin/figure/figureDetail";
    }

    @GetMapping("/figure/figureView/{id}")
    public String figureView(@PathVariable Long id, Model model) {
        FigureDto figureDto = figureService.findOne(id);
        model.addAttribute("figureDto", figureDto);
        return "admin/figure/figureView";
    }

    @PostMapping("/figure/update")
    public String updateFigure(
            @Valid FigureDto figureDto,
            MultipartFile file,
            BindingResult result
    ){
        if (result.hasErrors()) {
            return "admin/figure/figureDetail";
        }

        // 파일 업로드 및 현재 파일 삭제
        String fileName = null;
        try {
            if (file != null && !file.isEmpty()) {
                fileName = fileService.uploadFile(file);
                fileService.deleteFile(figureDto.getFileName());
            }
        } catch (IOException e) {
        }

        // figure Entity 수정
        if (file != null && !file.isEmpty()) {
            figureService.updateFigure(figureDto, file.getOriginalFilename(), fileName);
        } else {
            figureService.updateFigure(
                    figureDto, figureDto.getOriginalFileName(),
                    figureDto.getFileName()
            );
        }

        return "redirect:/admin/figures";
    }

    @GetMapping("/figure/downloadFile/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Resource resource = null;
        try {
            resource = fileService.downloadFile(fileName);
        } catch (IOException e) {
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @GetMapping({"/figure/delete/{id}", "/figure/delete/{id}/{fileName}"})
    public String deleteFigure(@PathVariable Long id,
                               @PathVariable(required = false) String fileName) {
        //첨부 파일 삭제
        try {
            if (fileName != null) {
                fileService.deleteFile(fileName);
            }
        } catch (IOException e) {
        }

        figureService.deleteFigure(id);
        return "redirect:/admin/figures";
    }
}
