package com.project.contap.model.chat;

import java.util.List;

public interface CustomChatMessageRepository {
    List<ChatMessage> findMessage(String roomId, Long startId);
    ChatMessage findLastMessage(String roomId);
}
