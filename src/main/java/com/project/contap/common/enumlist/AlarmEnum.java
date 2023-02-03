package com.project.contap.common.enumlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum AlarmEnum {
    TAP_RECEIVE(0),
    REJECT_TAP(1),
    ACCEPT_TAP(2),
    CHAT(3);
    private final int value;
}
