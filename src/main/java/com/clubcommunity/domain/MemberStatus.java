package com.clubcommunity.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus {
    WITHDRAW("탈퇴"),
    ACTIVITY("활동");

    private final String text;
}
