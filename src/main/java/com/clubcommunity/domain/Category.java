package com.clubcommunity.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    PHOTO("사진"), 
    VIDEO("영상"), 
    RECRUIT("부원모집"), 
    NOTICE("공지");

    private final String text;
}
