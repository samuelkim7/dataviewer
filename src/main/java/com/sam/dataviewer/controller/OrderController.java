package com.sam.dataviewer.controller;

import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.form.OrderForm;
import com.sam.dataviewer.repository.OrderRepository;
import com.sam.dataviewer.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @GetMapping("/order/new")
    public String createForm(Model model) {
        model.addAttribute("orderForm", new OrderForm());
        return "order/orderForm";
    }

    @PostMapping("/order/new")
    public String createOrder(@Valid OrderForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "order/orderForm";
        }

        Order order = new Order();

        return "redirect:/";
    }
}
