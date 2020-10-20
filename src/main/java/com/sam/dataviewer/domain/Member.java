package com.sam.dataviewer.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue
    private Long Id;

    @Column(name = "user_id")
    private String userId;

    private String password;

    private String name;

    private String email;

    private String phone;

    @Column(name = "birth_date")
    private LocalDateTime birthdate;

    private String address;

    private String zipcode;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
