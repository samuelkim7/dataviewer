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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/order/new")
    public String createForm(Model model) {
        model.addAttribute("orderForm", new OrderForm());
        return "order/createOrderForm";
    }

    @PostMapping("/order/new")
    public String createOrder(@RequestParam("principal") String principal,
                              @Valid OrderForm orderForm,
                              BindingResult result) {

        if (result.hasErrors()) {
            return "order/createOrderForm";
        }

        orderService.order(principal, orderForm);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@RequestParam("principal") String principal, Model model) {
        model.getAttribute("principal");
        List<Order> orders = orderService.findOrders(principal);
        model.addAttribute("orders", orders);

        return "order/orderList";
    }
}
