package com.project.contap.model.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDTO {
    private String roomId;
    private String message;
    private String writer;
    private String reciever;
    private int type = 0;  // 0 : 둘다 // 1 : 1명만이고 나머지는 로그인 상태 //2 : 한명만이고 나머지는 로그아웃 상태
    private String sessionId;
    private String writerSessionId;
    // 이상하게 여기다가 다른 코드를 넣으면 pub/sub이 동작을 안함.. 그냥 내비두자 이클래스는
}
