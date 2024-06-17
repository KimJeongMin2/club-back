package com.clubcommunity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClubRejectDTO {

    private Long clubId;
    private String refusalReason;

}
