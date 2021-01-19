package com.sam.dataviewer.adminController;

import com.sam.dataviewer.dto.DashboardDto;
import com.sam.dataviewer.dto.FigureDto;
import com.sam.dataviewer.exception.CustomException;
import com.sam.dataviewer.service.DashboardService;
import com.sam.dataviewer.service.FigureService;
import com.sam.dataviewer.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

        if (file != null && !file.isEmpty()) {
            // 파일 업로드 및 figure Entity 생성
            String fileName = fileService.uploadFile(file);
            figureService.create(dashboardId, figureDto, file.getOriginalFilename(), fileName);
        } else {
            // figure Entity 생성
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


        if (file != null && !file.isEmpty()) {
            // 파일 업로드 및 현재 파일 삭제, figure Entity 수정
            String fileName = fileService.uploadFile(file);
            fileService.deleteFile(figureDto.getFileName());
            figureService.updateFigure(figureDto, file.getOriginalFilename(), fileName);
        } else {
            // figure Entity 수정
            figureService.updateFigure(
                    figureDto, figureDto.getOriginalFileName(),
                    figureDto.getFileName()
            );
        }
        return "redirect:/admin/figures";
    }

    @GetMapping("/figure/downloadFile/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Resource resource = fileService.downloadFile(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @GetMapping({"/figure/delete/{id}", "/figure/delete/{id}/{fileName}"})
    public String deleteFigure(@PathVariable Long id,
                               @PathVariable(required = false) String fileName) {
        if (fileName != null) {
            // 첨부 파일 삭제
            fileService.deleteFile(fileName);
        }
        // figure Entity 삭제
        figureService.deleteFigure(id);
        return "redirect:/admin/figures";
    }

    @ExceptionHandler(CustomException.class)
    public String handleCustomException(CustomException e, Model model) {
        model.addAttribute("error", e.getMessage());
        log.error("handleCustomException", e);
        return "error";
    }
}
