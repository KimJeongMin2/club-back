package com.clubcommunity.dto;

import com.clubcommunity.domain.Gender;
import com.clubcommunity.domain.RoleType;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class MemberDTO {
    private String uid;
    private String pw;
    private Long studentId;
    private String name;
    private Long birth;
    private Gender gender;
    private String department;
    private String phoneNum;
    private String email;
    private RoleType roleType;

    @Override
    public String toString() {
        return "MemberDTO{" +
                "uid='" + uid + '\'' +
                ", studentId=" + studentId +
                ", name='" + name + '\'' +
                ", birth=" + birth +
                ", gender=" + gender +
                ", department='" + department + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", email='" + email + '\'' +
                ", roleType=" + roleType +
                '}';
    }


}
