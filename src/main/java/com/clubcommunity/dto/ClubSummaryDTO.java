package com.clubcommunity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ClubSummaryDTO {
    private Long clubId;
    private List<ClubJoinMemberDTO> members;
}