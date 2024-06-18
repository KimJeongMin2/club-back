package com.clubcommunity.dto;

import com.clubcommunity.domain.Type;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class ClubDetailDTO {
    private Long clubId;
    private String clubName;
    private String introduction;
    private String history;
    private String meetingTime;
    private byte[] registration;
    private byte[] photo;
    private byte[] staffList;
    private Type type; // 동아리 종류
    private String applicantName; // 신청자 이름
    private String applicantDepartment; //신청자 학과
    private Long applicantId; //신청자 학번
    private String applicantPhone; //신청자 전화번호
    private String professorName;
    private String professorMajor;
    private String professorPhone;

}
