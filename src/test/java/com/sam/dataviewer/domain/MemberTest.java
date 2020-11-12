package com.sam.dataviewer.domain;

import com.sam.dataviewer.dto.MemberDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class MemberTest {

    @Test
    public void createMember() throws Exception {
        //given
        String username = "kim";
        String password = "1234";
        String name = "김삿갓";
        LocalDate birthDate = LocalDate.now();

        //when
        Member member = Member.createMember(
                username, password,
                name, null, null,
                birthDate, null);

        //then
        assertThat(username).isEqualTo(member.getUsername());
        assertThat(password).isEqualTo(member.getPassword());
        assertThat(name).isEqualTo(member.getName());
        assertThat(birthDate).isEqualTo(member.getBirthDate());
    }

    @Test
    public void toDto() throws Exception {
        //given
        Member member = Member.createMember(
                "kim", null,
                null, "kim@gmail.com",
                "010-1234-5678",
                null, "대한민국 서울시"
        );

        //when
        MemberDto dto = member.toDto();

        //then
        assertThat(member.getUsername()).isEqualTo(dto.getUsername());
        assertThat(member.getEmail()).isEqualTo(dto.getEmail());
        assertThat(member.getPhoneNumber()).isEqualTo(dto.getPhoneNumber());
        assertThat(member.getAddress()).isEqualTo(dto.getAddress());
    }

    @Test
    public void update() throws Exception {
        //given
        Member member = Member.createMember(
                "kim", null,
                null, "kim@gmail.com",
                "010-1234-5678",
                null, "대한민국 서울시"
        );

        String newEmail = "sam@gmail.com";
        String newPhoneNumber = "010-1111-2222";
        String newAddress = "미국 텍사스주";

        //when
        member.update(
                null, null,
                newEmail, newPhoneNumber,
                null, newAddress
        );

        //then
        assertThat(newEmail).isEqualTo(member.getEmail());
        assertThat(newPhoneNumber).isEqualTo(member.getPhoneNumber());
        assertThat(newAddress).isEqualTo(member.getAddress());
    }

}