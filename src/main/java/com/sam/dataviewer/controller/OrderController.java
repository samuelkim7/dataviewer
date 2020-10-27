package com.sam.dataviewer.controller;

import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.form.OrderForm;
import com.sam.dataviewer.repository.OrderRepository;
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
    public String createForm(Model model) {
        model.addAttribute("orderForm", new OrderForm());
        return "order/createOrderForm";
    }

    @PostMapping("/order/new")
    public String createOrder(Principal principal,
                              @Valid OrderForm orderForm,
                              BindingResult result) {

        if (result.hasErrors()) {
            return "order/createOrderForm";
        }

        orderService.order(principal.getName(), orderForm);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(Principal principal, Model model) {
        List<OrderForm> orderForms = orderService.findOrdersByUsername(principal.getName());
        model.addAttribute("orderForms", orderForms);
        return "order/orderList";
    }

    @GetMapping("/order/orderDetail/{id}")
    public String orderDetail(@PathVariable Long id, Model model){
        OrderForm orderForm = orderService.findOne(id);
        model.addAttribute("orderForm", orderForm);
        return "order/orderDetail";
    }

    @PostMapping("/order/update")
    public String updateOrder(@Valid OrderForm orderForm) {
        orderService.updateOrder(orderForm);
        return "redirect:/orders";
    }

    @GetMapping("/order/cancel/{id}")
    public String cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return "redirect:/orders";
    }
}
