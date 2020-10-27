package com.sam.dataviewer.domain;

import com.sam.dataviewer.dto.MemberDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
// 기본 생성자 protected로 접근 제한
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {
    private static final long serialVersionUID = 1l;

    @Id
    @Column(name = "member_id")
    @GeneratedValue
    private Long Id;

    private String username;

    private String password;

    private String name;

    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private String address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    /*
    * 생성 메서드
    */
    public static Member createMember(
            String username, String encodedPassword,
            String name, String email,
            String phoneNumber, LocalDate birthDate,
            String address
    ) {
        Member member = new Member();
        member.username = username;
        member.password = encodedPassword;
        member.name = name;
        member.email = email;
        member.phoneNumber = phoneNumber;
        member.birthDate = birthDate;
        member.address = address;
        return member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /* dto Object로 변환 */
    public MemberDto toDto() {
        MemberDto dto = new MemberDto();
        dto.setUsername(this.getUsername());
        dto.setName(this.getName());
        dto.setEmail(this.email);
        dto.setPhoneNumber(this.getPhoneNumber());
        dto.setBirthDate(this.getBirthDate());
        dto.setAddress(this.getAddress());
        return dto;
    }

    /* 회원 정보 수정 */
    public void update(
            String encodedPassword, String name,
            String email, String phoneNumber,
            LocalDate birthDate, String address
    ) {
        this.password = encodedPassword;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.address = address;
    }
}
