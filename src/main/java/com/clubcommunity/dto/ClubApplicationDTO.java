package com.clubcommunity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClubApplicationDTO {

    private Long clubId;
    private String type; // 동아리 종류
    private String clubName; // 동아리 이름
    private String applicantName; // 신청자 이름
    private String applicantDepartment; //신청자 학과
    private Long applicantId; //신청자 학번
    private String applicantPhone; //신청자 전화번호
    private String professorName;
    private String professorMajor;
    private String professorPhone;
    private String clubStatus;
}