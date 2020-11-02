package com.sam.dataviewer.controller;

import com.sam.dataviewer.dto.EstimateDto;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.service.EstimateService;
import com.sam.dataviewer.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class EstimateController {

    private final EstimateService estimateService;
    private final OrderService orderService;

    @GetMapping("/estimates")
    public String estimateList(Principal principal, Model model) {
        List<EstimateDto> estimateDtos = estimateService.findEstimatesByUsername(principal.getName());
        model.addAttribute("estimateDtos", estimateDtos);
        return "estimate/estimateList";
    }

    @GetMapping("/estimate/estimateDetail/{id}")
    public String estimateDetail(@PathVariable Long id, Model model){
        OrderDto orderDto = orderService.findOrderByEstimateId(id);
        EstimateDto estimateDto = estimateService.findOne(id);
        model.addAttribute("orderDto", orderDto);
        model.addAttribute("estimateDto", estimateDto);
        return "estimate/estimateDetail";
    }

    @PostMapping("/estimate/accept")
    public String updateOrder(
            @RequestParam("orderDtoId") Long orderId,
            @RequestParam("id") Long estimateId
            ) {
        System.out.println(orderId);
        orderService.startOrder(orderId);
        estimateService.acceptEstimate(estimateId);
        return "redirect:/estimates";
    }

    @GetMapping("/estimate/cancel/{id}")
    public String cancelOrder(@PathVariable Long id) {
        estimateService.cancelEstimate(id);
        return "redirect:/estimates";
    }
}
