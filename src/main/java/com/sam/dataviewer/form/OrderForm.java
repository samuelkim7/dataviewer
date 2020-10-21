package com.sam.dataviewer.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class OrderForm {

    @NotEmpty(message = "제목을 기입해주세요.")
    private String title;

    @NotEmpty(message = "분석 의뢰 사항을 기입해주세요.")
    private String content;

    private String file;

}
