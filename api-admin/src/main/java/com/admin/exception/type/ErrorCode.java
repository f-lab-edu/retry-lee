package com.admin.exception.type;

import lombok.Getter;

@Getter
public enum ErrorCode {

    ERROR_BE1001("BE1001", "중복된 위치의 숙소입니다.");


    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
