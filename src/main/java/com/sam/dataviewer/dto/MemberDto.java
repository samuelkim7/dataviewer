package com.sam.dataviewer.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter @Setter
public class MemberDto {

    @NotEmpty(message = "필수 정보입니다.")
    private String username;

    @Size(min = 4, message = "비밀번호는 4자리 이상이어야 합니다.")
    private String password;

    @NotEmpty(message = "필수 정보입니다.")
    private String name;

    @NotEmpty(message = "필수 정보입니다.")
    @Email(message = "이메일 형식을 맞춰서 입력해주세요.")
    private String email;

    @NotEmpty(message = "필수 정보입니다.")
    private String phoneNumber;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;

    private String address;
}
