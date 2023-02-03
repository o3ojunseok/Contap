package com.project.contap.common.enumlist;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum AuthorityEnum {

    PHONE_TUTORIAL(Authority.PHONE_TUTORIAL),
    PROFILE_TUTORIAL(Authority.PROFILE_TUTORIAL),
    CAN_OTHER_READ(Authority.CAN_OTHER_READ),
    ALARM(Authority.ALARM),
    ALL_AUTHORITY(Authority.ALL_AUTHORITY);

    int authority;

    AuthorityEnum(int authority) {
        this.authority = authority;
    }

    public int getAuthority() {
        return this.authority;
    }


    public static class Authority {
        public static final int PHONE_TUTORIAL = 0b0001;
        public static final int PROFILE_TUTORIAL = 0b0010;
        public static final int CAN_OTHER_READ = 0b0100;
        public static final int ALARM = 0b1000;
        public static final int ALL_AUTHORITY = 0b1111;
    }

}
