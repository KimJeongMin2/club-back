package com.clubcommunity.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    MANAGER("관리자"),
    MASTER("마스터관리자"),
    MEMBER("일반회원");

    private final String text;
}
