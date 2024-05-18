package com.clubcommunity.dto;

import com.clubcommunity.domain.Gender;
import com.clubcommunity.domain.RoleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
    private Long studentId;
    private String name;
    private Long birth;
    private Gender gender;
    private String department;
    private String phoneNum;
    private String email;
    private RoleType roleType;
}
