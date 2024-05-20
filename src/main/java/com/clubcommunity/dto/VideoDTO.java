package com.clubcommunity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class VideoDTO {

    private String title; // 동영상 제목
    private String content; // 동영상 url
    private MemberDTO member;
}
