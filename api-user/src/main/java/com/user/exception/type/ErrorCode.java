package com.user.exception.type;

import lombok.Getter;

@Getter
public enum ErrorCode {

    ERROR_BE1005("BE1005", "유효하지 않은 토큰입니다."),
    ERROR_BE1004("BE1004", "사용자를 찾을 수 없습니다."),
    ERROR_BE1003("BE1003", "로그인 실패, 이메일이나 비밀번호를 확인해주세요."),
    ERROR_BE1002("BE1002", "입력값 검증 실패"),
    ERROR_BE1001("BE1001", "회원가입 실패(이메일 중복), 이메일을 확인해주세요.");


    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
