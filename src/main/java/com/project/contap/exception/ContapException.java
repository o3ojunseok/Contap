package com.project.contap.exception;


import lombok.Getter;

@Getter
public class ContapException extends RuntimeException {
    // 필요에 따라서 변수를 추가해주세요 !
    private final ErrorCode errorCode;
    private Object data = "";

    public ContapException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ContapException(ErrorCode errorCode,Object data) {
        this.errorCode = errorCode;
        this.data = data;
    }
}
