package com.project.contap.chat;

import com.project.contap.model.chat.ChatMessage;
import com.project.contap.model.chat.ChatMessageDTO;
import com.project.contap.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@Controller
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void message(SimpMessageHeaderAccessor headerAccessor,ChatMessageDTO message) {
        chatService.publish(message,headerAccessor.getUser().getName());
    }

    @ResponseBody
    @GetMapping("/chat/getmsg/{roomId}/{longId}")
    public List<ChatMessage> getmessage_30(@PathVariable String roomId,@PathVariable Long longId) {
        return chatService.findMessage(roomId,longId);// 권한체크 필수
    }
}