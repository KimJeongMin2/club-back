package com.clubcommunity.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    GO_OVER("검토"),
    APPROVAL("승인"),
    REFUSE("거절");

    private final String text;
}
