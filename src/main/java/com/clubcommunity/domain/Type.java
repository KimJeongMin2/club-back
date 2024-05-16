package com.clubcommunity.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Type {
    DEPARTMENT("학과"),
    CENTER("중앙");

    private final String text;
}
