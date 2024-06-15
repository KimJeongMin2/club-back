package com.clubcommunity.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeVisibilityType {
    ENTIRE("전체공개"),
    CLUB("동아리 공개");

    private final String text;
}
