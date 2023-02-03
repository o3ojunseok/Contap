package com.project.contap.model.chat;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, CustomChatMessageRepository {
    List<ChatMessage> findAllByRoomId(String roomId);
}
