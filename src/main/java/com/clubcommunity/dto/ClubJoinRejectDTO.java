package com.clubcommunity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ClubJoinRejectDTO {
    private Long clubJoinId;
    private List<Long> clubJoinIds;
    private String refusalReason;
}
