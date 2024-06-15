package com.clubcommunity.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Column(name = "id")
    private String uid;
    private String pw;

    @Id
    @Column(name = "student_id")
    private Long studentId;

    private String name;
    private Long birth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String department;

    @Column(name = "phone_num")
    private String phoneNum;
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type")
    private RoleType roleType;


    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClubMember> clubMembers = new ArrayList<>();
}