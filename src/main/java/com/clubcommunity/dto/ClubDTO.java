package com.clubcommunity.dto;

import com.clubcommunity.domain.Type;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@NoArgsConstructor
@Getter
public class ClubDTO {
    private Type type; // 동아리 종류
    private String clubName; // 동아리 이름
    private String applicantName; // 신청자 이름
    private String applicantDepartment; //신청자 학과
    private Long applicantId; //신청자 학번
    private String applicantPhone; //신청자 전화번호
    private String professorName;
    private String professorMajor;
    private String professorPhone;
    private MemberDTO member;
}
