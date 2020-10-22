package com.sam.dataviewer.controller;

import com.sam.dataviewer.form.MemberForm;
import com.sam.dataviewer.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/member/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "member/createMemberForm";
    }

    @PostMapping("/member/new")
    public String createMember(@Valid MemberForm memberForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/createMemberForm";
        }
        try {
            memberService.join(memberForm);
        } catch (IllegalStateException e) {
            bindingResult.rejectValue("username", "duplication", "이미 존재하는 아이디입니다.");
        }
        return "/";
    }
}
