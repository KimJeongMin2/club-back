package com.clubcommunity.dto;

import com.clubcommunity.domain.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ClubJoinMemberDTO {
    private Long clubJoinId;
    private MemberDTO member;
    private Status status;
}
