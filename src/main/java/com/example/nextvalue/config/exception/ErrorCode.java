package com.example.nextvalue.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다."),
    RESOURCE_NOT_FOUND(404, "C002", "리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "C003", "서버 에러가 발생했습니다."),
    UNAUTHORIZED_ACTION(401, "C004", "해당 작업에 대한 권한이 없습니다."),

    // user
    DUPLICATE_EMAIL(400,"U001", "중복된 이메일입니다."),
    DUPLICATE_NICKNAME(400, "U002", "중복된 닉네임입니다."),

    // follow
    SELF_FOLLOW_NOT_ALLOWED(400, "F001", "자기 자신을 팔로우할 수 없습니다."),
    ALREADY_FOLLOWING(400, "F002", "이미 팔로우하고 있는 사용자입니다."),
    NOT_FOLLOWING(400, "F003", "팔로우하고 있지 않은 사용자입니다."),


    USER_NOT_FOUND(400, "U003", "존재하지 않는 사용자입니다."),
    INVALID_PASSWORD(400, "U004", "비밀번호가 일치하지 않습니다."),

    // letter
    INVALID_OPEN_TIME(400, "L001", "현재 날짜는 편지 열기 가능한 기간이 아닙니다."),


    //TASK
    TASK_NOT_FOUND(404, "T003", "존재하지 않는 작업입니다."),
    ;
    private final int status;
    private final String code;
    private final String message;
}
