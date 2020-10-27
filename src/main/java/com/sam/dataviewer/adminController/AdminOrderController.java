package com.sam.dataviewer.adminController;

import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public String orderList(Model model) {
        List<OrderDto> orderDtos = orderService.findAll();
        model.addAttribute("orderDtos", orderDtos);
        return "admin/order/orderList";
    }

    @GetMapping("/order/orderDetail/{id}")
    public String orderDetail(@PathVariable Long id, Model model){
        OrderDto orderDto = orderService.findOne(id);
        model.addAttribute("orderDto", orderDto);
        return "admin/order/orderDetail";
    }
}
