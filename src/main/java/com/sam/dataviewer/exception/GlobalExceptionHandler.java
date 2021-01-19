/* 해당 Controller에 ExceptionHandler 구현하는 것으로 대체 */
//package com.sam.dataviewer.exception;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice
//@Slf4j
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(CustomException.class)
//    public String handleCustomException(CustomException e, Model model) {
//        model.addAttribute("error", e.getMessage());
//        log.error("handleCustomException", e);
//        return "error";
//    }
//}
