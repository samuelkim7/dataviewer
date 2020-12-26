package com.sam.dataviewer.domain;

import com.sam.dataviewer.dto.MemberDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.BDDAssertions.then;

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
        then(username).isEqualTo(member.getUsername());
        then(password).isEqualTo(member.getPassword());
        then(name).isEqualTo(member.getName());
        then(birthDate).isEqualTo(member.getBirthDate());
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
        then(member.getUsername()).isEqualTo(dto.getUsername());
        then(member.getEmail()).isEqualTo(dto.getEmail());
        then(member.getPhoneNumber()).isEqualTo(dto.getPhoneNumber());
        then(member.getAddress()).isEqualTo(dto.getAddress());
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
                null, newEmail, newPhoneNumber,
                null, newAddress
        );

        //then
        then(newEmail).isEqualTo(member.getEmail());
        then(newPhoneNumber).isEqualTo(member.getPhoneNumber());
        then(newAddress).isEqualTo(member.getAddress());
    }

    @Test
    public void updatePassword() throws Exception {
        //given
        Member member = Member.createMember(
                "kim", "1111",
                null, null, null,
                null, null
        );

        String newPassword = "2222";

        //when
        member.updatePassword(newPassword);

        //then
        then(newPassword).isEqualTo(member.getPassword());
    }

}