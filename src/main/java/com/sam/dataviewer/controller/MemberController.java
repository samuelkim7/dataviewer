package com.sam.dataviewer.controller;

import com.sam.dataviewer.dto.MemberDto;
import com.sam.dataviewer.dto.PasswordDto;
import com.sam.dataviewer.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/member/new")
    public String createForm(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "member/createMemberForm";
    }

    @PostMapping("/member/new")
    public String createMember(@Valid MemberDto memberDto, BindingResult result) {
        if (result.hasErrors()) {
            return "member/createMemberForm";
        }
        try {
            memberService.join(memberDto);
        } catch (IllegalStateException e) {
            result.rejectValue("username", "duplication", "이미 존재하는 아이디입니다.");
            return "member/createMemberForm";
        }
        return "redirect:/login";
    }

    @GetMapping("/member/memberDetail")
    public String memberDetail(Principal principal, Model model) {
        MemberDto memberDto = memberService.findOne(principal.getName());
        model.addAttribute("memberDto", memberDto);
        return "member/memberDetail";
    }

    @PostMapping("/member/update")
    public String updateMember(@Valid MemberDto memberDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/memberDetail";
        }
        memberService.updateMember(memberDto);
        return "redirect:/member/memberDetail";
    }

    @GetMapping("/member/updatePassword")
    public String updateForm(Model model) {
        model.addAttribute("passwordDto", new PasswordDto());
        return "member/updatePasswordForm";
    }

    @PostMapping("/member/updatePassword")
    public String updatePassword(
            Principal principal,
            @Valid PasswordDto passwordDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "member/updatePasswordForm";
        }
        try {
            memberService.updatePassword(principal.getName(), passwordDto);
        } catch (IllegalStateException e) {
            bindingResult.rejectValue("currentPassword", "discord", "기존 비밀번호가 틀렸습니다.");
            return "member/updatePasswordForm";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("confirmPassword", "discord", "재확인용 비밀번호가 일치하지 않습니다.");
            return "member/updatePasswordForm";
        }
        return "redirect:/login";
    }
}
