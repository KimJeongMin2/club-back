package com.clubcommunity.dto;

import com.clubcommunity.domain.MemberStatus;
import com.clubcommunity.domain.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class ClubJoinMemberDTO {
    private Long clubJoinId;
    private MemberDTO member;
    private ClubDTO club;
    private Status status;
    private MemberStatus memberStatus;
}
