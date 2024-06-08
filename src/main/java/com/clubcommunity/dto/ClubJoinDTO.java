package com.clubcommunity.dto;

import com.clubcommunity.domain.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ClubJoinDTO {
    private Long clubJoinId;

    private List<Long> clubJoinIds;
    private String title;
    private LocalDateTime createdAt;
    private Status status;
    private MemberDTO member;
    private ClubDTO club;
    private byte[] file;

    private String uploadFileName;
    private String refusalReason;
}
