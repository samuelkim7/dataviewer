package com.sam.dataviewer.controller;

import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/order/new")
    public String createDto(Model model) {
        model.addAttribute("orderDto", new OrderDto());
        return "order/createOrderForm";
    }

    @PostMapping("/order/new")
    public String createOrder(Principal principal,
                              @Valid OrderDto orderDto,
                              BindingResult result) {

        if (result.hasErrors()) {
            return "order/createOrderForm";
        }

        orderService.order(principal.getName(), orderDto);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(Principal principal, Model model) {
        List<OrderDto> orderDtos = orderService.findOrdersByUsername(principal.getName());
        model.addAttribute("orderDtos", orderDtos);
        return "order/orderList";
    }

    @GetMapping("/order/orderDetail/{id}")
    public String orderDetail(@PathVariable Long id, Model model){
        OrderDto orderDto = orderService.findOne(id);
        model.addAttribute("orderDto", orderDto);
        return "order/orderDetail";
    }

    @PostMapping("/order/update")
    public String updateOrder(@Valid OrderDto orderDto) {
        orderService.updateOrder(orderDto);
        return "redirect:/orders";
    }

    @GetMapping("/order/cancel/{id}")
    public String cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return "redirect:/orders";
    }
}
