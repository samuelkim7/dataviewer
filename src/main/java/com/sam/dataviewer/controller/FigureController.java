package com.sam.dataviewer.controller;

import com.sam.dataviewer.dto.FigureDto;
import com.sam.dataviewer.service.FigureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class FigureController {

    private final FigureService figureService;

    @GetMapping("/figure/figureView/{id}")
    public String figureView(@PathVariable Long id, Model model) {
        FigureDto figureDto = figureService.findOne(id);
        model.addAttribute("figureDto", figureDto);
        return "figure/figureView";
    }
}

