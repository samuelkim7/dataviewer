package com.sam.dataviewer.controller;

import com.sam.dataviewer.dto.DashboardDto;
import com.sam.dataviewer.dto.FigureDto;
import com.sam.dataviewer.service.DashboardService;
import com.sam.dataviewer.service.FigureService;
import com.sam.dataviewer.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final FigureService figureService;

    @GetMapping("/dashboards")
    public String dashboardList(Principal principal, Model model) {
        List<DashboardDto> dashboardDtos =
                dashboardService.findByUsername(principal.getName());
        model.addAttribute("dashboardDtos", dashboardDtos);
        return "dashboard/dashboardList";
    }

    @GetMapping("/dashboard/dashboardDetail/{id}")
    public String dashboardDetail(@PathVariable Long id, Model model) {
        DashboardDto dashboardDto = dashboardService.findOne(id);
        List<FigureDto> figureDtos = figureService.findByDashboard(id);
        model.addAttribute("dashboardDto", dashboardDto);
        model.addAttribute("figureDtos", figureDtos);
        return "dashboard/dashboardDetail";
    }
}
