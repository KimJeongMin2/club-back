package com.clubcommunity.dto;

import com.clubcommunity.domain.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class ClubJoinDTO {
    private Long clubJoinId;
    private String title;
    private LocalDateTime createdAt;
    private Status status;
    private MemberDTO member;
    private ClubDTO club;
}
