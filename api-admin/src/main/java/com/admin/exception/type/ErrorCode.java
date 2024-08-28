package com.admin.exception.type;

import lombok.Getter;

@Getter
public enum ErrorCode {

    ERROR_BE1002("BE1002", "해당 국가코드가 존재하지 않습니다."),
    ERROR_BE1001("BE1001", "중복된 위치의 숙소입니다.");


    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
