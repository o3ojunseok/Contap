package com.project.contap.common.enumlist;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DefaultRspEnum {
    CANT_SEND_TO_ME("자신한테 탭요청하지못해요"),
    ALREADY_FRIEND("이미 친구 관계입니다."),
    ACCEPT_TAP("상대방의 요청을 수락 하였습니다."),
    ALREADY_SEND("이미 상대에게 요청을 보낸 상태입니다."),
    OK("정상적으로 처리 되었습니다."),
    NOT_FOUND_TAP("해당 tab이 존재하지 않습니다 TabID를 다시확인해주세요.."),
    WRONG("잘못된 요청입니다.");
    private final String value;
}
