package com.sam.dataviewer.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 아이디를 입력해주세요.")
    private String username;

    @Size(min = 4, message = "비밀번호는 4자리 이상이어야 합니다.")
    private String password;

    @NotEmpty(message = "이름을 입력해주세요.")
    private String name;

    @Email(message = "이메일 형식을 맞춰서 입력해주세요.")
    private String email;

    @NotEmpty(message = "전화번호를 입력해주세요.")
    private String phone;

    @NotEmpty(message = "생년월일을 입력해주세요.")
    private LocalDateTime birthdate;

    private String address;

    private String zipcode;
}
