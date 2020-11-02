package com.sam.dataviewer.adminController;

import com.sam.dataviewer.dto.DashboardDto;
import com.sam.dataviewer.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminDashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard/new")
    public String createForm(Model model) {
        model.addAttribute("dashboardDto", new DashboardDto());
        return "admin/dashboard/createDashboardForm";
    }

    @PostMapping("/dashboard/new")
    public String createDashboard(
            @Valid DashboardDto dashboardDto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return "admin/dashboard/createDashboardForm";
        }
        dashboardService.create(dashboardDto);
        return "redirect:/admin/dashboards";
    }

}
