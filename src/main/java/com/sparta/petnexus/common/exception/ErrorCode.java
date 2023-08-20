package com.sparta.petnexus.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // user
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "U001", "존재하지 않는 사용자입니다."),
    EXISTED_EMAIL(HttpStatus.CONFLICT, "U002", "중복된 이메일 입니다."),
    BAD_ID_PASSWORD(HttpStatus.BAD_REQUEST, "U003", "아이디나 비밀번호가 맞지 않습니다."),
    DO_NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "U004", "비밀번호가 일치하지 않습니다."),
    //post
    NOT_FOUND_POST(HttpStatus.BAD_REQUEST,"P001","존재하지 않는 post 입니다."),
    NOT_POST_UPDATE(HttpStatus.BAD_REQUEST,"P002","작셩자만 수정할 수 있습니다."),
    NOT_POST_DELETE(HttpStatus.BAD_REQUEST,"P003","작셩자만 삭제할 수 있습니다."),
    // trade
    NOT_FOUND_TRADE(HttpStatus.BAD_REQUEST, "T001", "존재하지 않는 거래 게시글입니다."),
    NOT_TRADE_UPDATE(HttpStatus.BAD_REQUEST,"T002","작셩자만 수정할 수 있습니다."),
    NOT_TRADE_DELETE(HttpStatus.BAD_REQUEST,"T003","작셩자만 삭제할 수 있습니다."),
    EXISTED_LIKE(HttpStatus.CONFLICT, "T004", "이미 좋아요가 생성되었습니다."),
    EXISTED_DISLIKE(HttpStatus.CONFLICT, "T005", "이미 좋아요 취소가 생성되었습니다."),
    //Token
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST,"TK001","잘못된 리프레쉬 토큰입니다. 재로그인 해주세요."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.BAD_REQUEST,"TK002","리프레쉬 토큰을 찾을 수 없습니다. 재로그인 해주세요.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}