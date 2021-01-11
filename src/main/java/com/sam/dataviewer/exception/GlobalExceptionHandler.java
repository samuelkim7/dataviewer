package com.sam.dataviewer.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public String handleCustomException(CustomException e, RedirectAttributes redirectAttributes) {
            redirectAttributes.addAttribute("message", e.getMessage());
        return "error";
    }
}
