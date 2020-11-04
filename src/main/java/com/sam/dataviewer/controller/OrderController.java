package com.sam.dataviewer.controller;

import com.sam.dataviewer.dto.FileDto;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.service.FileService;
import com.sam.dataviewer.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final FileService fileService;

    @GetMapping("/order/new")
    public String createForm(Model model) {
        model.addAttribute("orderDto", new OrderDto());
        return "order/createOrderForm";
    }

    @PostMapping("/order/new")
    public String createOrder(Principal principal,
                              @Valid OrderDto orderDto,
                              MultipartFile file,
                              BindingResult result) {

        if (result.hasErrors()) {
            return "order/createOrderForm";
        }

        Long orderId = orderService.order(principal.getName(), orderDto);

        try {
            fileService.saveFile(orderId, file);
        } catch (IOException e) {
            result.rejectValue("fileName", "IOException", "파일을 다시 한번 확인해보세요.");
        }
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
        List<FileDto> fileDtos = fileService.findByOrderId(id);
        model.addAttribute("orderDto", orderDto);
        model.addAttribute("fileDtos", fileDtos);
        return "order/orderDetail";
    }

    @PostMapping("/order/update")
    public String updateOrder(
            @Valid OrderDto orderDto,
            MultipartFile file,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return "order/orderDetail";
        }

        try {
            fileService.saveFile(orderDto.getId(), file);
        } catch (IOException e) {
            result.rejectValue("fileName", "IOException", "파일을 다시 한번 확인해보세요.");
        }
        orderService.updateOrder(orderDto);
        return "redirect:/orders";
    }

    @GetMapping("/order/cancel/{id}")
    public String cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return "redirect:/orders";
    }
}
