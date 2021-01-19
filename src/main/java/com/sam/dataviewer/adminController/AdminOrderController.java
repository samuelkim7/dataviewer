package com.sam.dataviewer.adminController;

import com.sam.dataviewer.dto.FileDto;
import com.sam.dataviewer.dto.MemberDto;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.exception.CustomException;
import com.sam.dataviewer.service.FileService;
import com.sam.dataviewer.service.MemberService;
import com.sam.dataviewer.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminOrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final FileService fileService;

    @GetMapping("/orders")
    public String orderList(Model model) {
        List<OrderDto> orderDtos = orderService.findAll();
        model.addAttribute("orderDtos", orderDtos);
        return "admin/order/orderList";
    }

    @GetMapping("/order/orderDetail/{id}")
    public String orderDetail(@PathVariable Long id, Model model){
        OrderDto orderDto = orderService.findOne(id);
        MemberDto memberDto = memberService.findMemberByOrderId(id);
        List<FileDto> fileDtos = fileService.findByOrderId(id);
        model.addAttribute("orderDto", orderDto);
        model.addAttribute("memberDto", memberDto);
        model.addAttribute("fileDtos", fileDtos);
        return "admin/order/orderDetail";
    }

    @GetMapping("/order/downloadFile/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Resource resource = fileService.downloadFile(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @ExceptionHandler(CustomException.class)
    public String handleCustomException(CustomException e, Model model) {
        model.addAttribute("error", e.getMessage());
        log.error("handleCustomException", e);
        return "error";
    }
}
