package com.sam.dataviewer.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter @Setter
public class PasswordDto {

    private String currentPassword;

    @Size(min = 4, message = "비밀번호는 4자리 이상이어야 합니다.")
    private String newPassword;

    private String confirmPassword;

    public PasswordDto() {
    }

    public PasswordDto(
            String currentPassword,
            @Size(min = 4, message = "비밀번호는 4자리 이상이어야 합니다.") String newPassword,
            String confirmPassword
    ) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
}
