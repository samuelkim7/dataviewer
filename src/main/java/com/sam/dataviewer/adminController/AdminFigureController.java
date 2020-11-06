package com.sam.dataviewer.adminController;

import com.sam.dataviewer.dto.DashboardDto;
import com.sam.dataviewer.dto.FigureDto;
import com.sam.dataviewer.service.DashboardService;
import com.sam.dataviewer.service.FigureService;
import com.sam.dataviewer.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

        if (!file.isEmpty()) {
            figureService.create(dashboardId, figureDto, file.getOriginalFilename());
        } else {
            figureService.create(dashboardId, figureDto, null);
        }

        try {
            if (!file.isEmpty()) {
                fileService.uploadFile(file);
            }
        } catch (IOException e) {
            result.rejectValue("fileName", "IOException", "파일을 다시 한번 확인해보세요.");
        }
        return "redirect:/admin/figures";
    }

    @GetMapping("/figures")
    public String figureList(Model model) {
        List<FigureDto> figureDtos = figureService.findAll();
        model.addAttribute("figureDtos", figureDtos);
        return "admin/figure/figureList";
    }
}
