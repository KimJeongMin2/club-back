package com.clubcommunity.dto;

import com.clubcommunity.domain.Gender;
import com.clubcommunity.domain.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
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
