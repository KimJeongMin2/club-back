package com.clubcommunity.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    private String name;
    private Long birth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String department;
    private String phoneNum;
    private String email;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

}
