package com.sam.dataviewer.controller;

import com.sam.dataviewer.dto.EstimateDto;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.service.EstimateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class EstimateController {

    private final EstimateService estimateService;

    @GetMapping("/estimates")
    public String estimateList(Principal principal, Model model) {
        List<EstimateDto> estimateDtos = estimateService.findEstimatesByUsername(principal.getName());
        model.addAttribute("estimateDtos", estimateDtos);
        return "estimate/estimateList";
    }

    @GetMapping("/estimate/estimateDetail/{id}")
    public String estimateDetail(@PathVariable Long id, Model model){
        OrderDto orderDto = estimateService.findOne(id);
        model.addAttribute("orderDto", orderDto);
        return "estimate/estimateDetail";
    }

    @GetMapping("/estimate/accept/{id}")
    public String updateOrder(@PathVariable Long id) {
        estimateService.acceptEstimate(id);
        return "redirect:/estimates";
    }

    @GetMapping("/estimate/cancel/{id}")
    public String cancelOrder(@PathVariable Long id) {
        estimateService.cancelEstimate(id);
        return "redirect:/estimates";
    }
}
