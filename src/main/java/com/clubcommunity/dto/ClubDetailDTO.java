package com.clubcommunity.dto;

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
}
