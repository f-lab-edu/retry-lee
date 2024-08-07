package com.user.utils.enums;

import lombok.Getter;

@Getter
public enum TokenType {
    ACCESS, REFRESH;

    private long expiredMs;

    public void setExpiredMs(long expiredMs) {
        this.expiredMs = expiredMs;
    }
}
