package com.sam.dataviewer.adminController;

import com.sam.dataviewer.dto.DashboardDto;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.service.DashboardService;
import com.sam.dataviewer.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminDashboardController {

    private final DashboardService dashboardService;
    private final OrderService orderService;

    @GetMapping("/dashboard/new")
    public String createForm(Model model) {
        List<OrderDto> orderDtos = orderService.findAll();
        model.addAttribute("orders", orderDtos);
        model.addAttribute("dashboardDto", new DashboardDto());
        return "admin/dashboard/createDashboardForm";
    }

    @PostMapping("/dashboard/new")
    public String createDashboard(
            @RequestParam("orderId") Long orderId,
            @Valid DashboardDto dashboardDto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return "admin/dashboard/createDashboardForm";
        }
        dashboardService.create(orderId, dashboardDto);
        return "redirect:/admin/dashboards";
    }

    @GetMapping("/dashboards")
    public String dashboardList(Model model) {
        List<DashboardDto> dashboardDtos = dashboardService.findAll();
        model.addAttribute("dashboardDtos", dashboardDtos);
        return "admin/dashboard/dashboardList";
    }

}
