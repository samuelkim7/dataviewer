package com.sam.dataviewer;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class Common {

    @ModelAttribute
    public void commonData(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("principal", principal.getName());
        }
    }
}
