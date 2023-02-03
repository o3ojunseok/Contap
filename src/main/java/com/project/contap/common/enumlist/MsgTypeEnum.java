package com.project.contap.common.enumlist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MsgTypeEnum {
    CHAT_BOTH(0b000001,"",null),
    CHAT_EITHER_LOGINON(0b000010,"",null),
    CHAT_EITHER_LOGOFF(0b000100,"",AlarmEnum.CHAT),
    SEND_TAP(0b001000,"탭요청을보냈어요",AlarmEnum.TAP_RECEIVE),
    REJECT_TAP(0b010000,"탭요청을 거절했네요!",AlarmEnum.REJECT_TAP),
    ACCEPT_TAP(0b100000,"탭요청을 수락했네요!",AlarmEnum.ACCEPT_TAP),
    TO_ALL(0b000111,"탭요청을 수락했네요!",AlarmEnum.ACCEPT_TAP),
    TO_USER(0b111010,"탭요청을 수락했네요!",AlarmEnum.ACCEPT_TAP);

    private final int value;
    private final String msg;
    private AlarmEnum alarmEnum;
}
