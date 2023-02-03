package com.project.contap.service;

import com.project.contap.common.Common;
import com.project.contap.common.DefaultRsp;
import com.project.contap.common.enumlist.AlarmEnum;
import com.project.contap.model.chat.ChatMessage;
import com.project.contap.model.chat.ChatMessageDTO;
import com.project.contap.model.chat.ChatMessageRepository;
import com.project.contap.model.chat.ChatRoomRepository;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {
    @Mock
    RedisTemplate redisTemplate;
    @Mock
    ChatMessageRepository chatMessageRepository;
    @Mock
    ChatRoomRepository chatRoomRepository;
    @Mock
    ChannelTopic channelTopic;
    private HashMap<String, List<ChatMessage>> updateRoomList2 = new HashMap<>(); // 메모리문제가있으려나 ..?
    private List<ChatMessage> chatmessages= new ArrayList<>(); // for bulk insert
    private String subPrefix = "/sub";
    private int subPrefixlen = 15;

    @Nested
    @DisplayName("채팅 내용 불러오기")
    class GetMsg {
        @Test
        @DisplayName("채팅방 최초 입장.")
        void GetMsg() {
            String roomId = "abc";
            ChatMessageDTO message1 = new ChatMessageDTO();
            message1.setMessage("msg1");
            message1.setRoomId(roomId);
            message1.setWriter("a");
            ChatMessageDTO message2 = new ChatMessageDTO();
            message2.setMessage("msg2");
            message2.setRoomId(roomId);
            message2.setWriter("a");
            ChatMessageDTO message3 = new ChatMessageDTO();
            message3.setMessage("msg3");
            message3.setRoomId(roomId);
            message3.setWriter("a");
            ChatService chatService = new ChatService(redisTemplate,chatMessageRepository,chatRoomRepository,channelTopic);
            when(chatRoomRepository.getChatUserCnt(any()))
                    .thenReturn(2);
            chatService.publish(message1,"sender");
            chatService.publish(message2,"sender");
            chatService.publish(message3,"sender");
            List<ChatMessage> chatmsg = new ArrayList<>();
            for(Long i = 1L ; i < 4L ; i++) {
                ChatMessage msg = new ChatMessage();
                msg.setMessage(String.format("%d",i));
                msg.setWriter("a");
                msg.setRoomId(roomId);
                msg.setId(i);
                msg.setCreatedDt(LocalDateTime.now());
                chatmsg.add(msg);
            }
            when(chatMessageRepository.findMessage(roomId,0L)).thenReturn(chatmsg);
            List<ChatMessage> ret = chatService.findMessage(roomId,0L);
            System.out.println(ret);
            for(ChatMessage chatMessage : ret)
            {
                assertEquals(roomId,chatMessage.getRoomId());
            }
        }

        @Test
        @DisplayName("채팅방 페이징기능")
        void GetMsg_page() {
            String roomId = "abc";
            ChatMessageDTO message1 = new ChatMessageDTO();
            message1.setMessage("msg1");
            message1.setRoomId(roomId);
            message1.setWriter("a");
            ChatMessageDTO message2 = new ChatMessageDTO();
            message2.setMessage("msg2");
            message2.setRoomId(roomId);
            message2.setWriter("a");
            ChatMessageDTO message3 = new ChatMessageDTO();
            message3.setMessage("msg3");
            message3.setRoomId(roomId);
            message3.setWriter("a");
            ChatService chatService = new ChatService(redisTemplate,chatMessageRepository,chatRoomRepository,channelTopic);
            when(chatRoomRepository.getChatUserCnt(any()))
                    .thenReturn(2);
            chatService.publish(message1,"sender");
            chatService.publish(message2,"sender");
            chatService.publish(message3,"sender");
            List<ChatMessage> chatmsg = new ArrayList<>();
            for(Long i = 1L ; i < 4L ; i++) {
                ChatMessage msg = new ChatMessage();
                msg.setMessage(String.format("%d",i));
                msg.setWriter("a");
                msg.setRoomId(roomId);
                msg.setId(i);
                msg.setCreatedDt(LocalDateTime.now());
                chatmsg.add(msg);
            }
            when(chatMessageRepository.findMessage(roomId,1L)).thenReturn(chatmsg);
            List<ChatMessage> ret = chatService.findMessage(roomId,1L);
            System.out.println(ret);

            assertEquals(chatmsg.size(),ret.size());
            for(int i = 0; i < 3; i++)
            {
                assertEquals(chatmsg.get(i).getId(),ret.get(i).getId());
            }
        }
    }
    @Nested
    @DisplayName("채팅 내용 불러오기")
    class pub {
        @Test
        @DisplayName("채팅방에 1명있고 나머지 로그인중일때")
        void pub1() {
            String roomId = "abc";
            ChatMessageDTO message1 = new ChatMessageDTO();
            message1.setMessage("msg1");
            message1.setRoomId(roomId);
            message1.setWriter("a");
            message1.setReciever("reciever");
            ChatService chatService = new ChatService(redisTemplate,chatMessageRepository,chatRoomRepository,channelTopic);
            when(chatRoomRepository.getChatUserCnt(eq(roomId)))
                    .thenReturn(1);
            when(chatRoomRepository.getSessionId(eq(message1.getReciever())))
                    .thenReturn("asdsagfasf");
            when(channelTopic.getTopic())
                    .thenReturn("asdsagfasf2");
            chatService.publish(message1,"sender");

            verify(chatRoomRepository,times(1)).newMsg(message1.getRoomId(),message1.getWriter(),message1.getReciever(),message1.getType(),message1.getMessage());//any(User.class)
            verify(redisTemplate,times(1)).convertAndSend(eq("asdsagfasf2"),eq(message1));//any(User.class)

        }
        @Test
        @DisplayName("채팅방에 1명있고 나머지 로그인중아닐때")
        void pub2() {
            String roomId = "abc";
            ChatMessageDTO message1 = new ChatMessageDTO();
            message1.setMessage("msg1");
            message1.setRoomId(roomId);
            message1.setWriter("a");
            message1.setReciever("reciever");
            ChatService chatService = new ChatService(redisTemplate,chatMessageRepository,chatRoomRepository,channelTopic);
            when(chatRoomRepository.getChatUserCnt(eq(roomId)))
                    .thenReturn(1);
            when(channelTopic.getTopic())
                    .thenReturn("asdsagfasf");
            chatService.publish(message1,"sender");

            verify(chatRoomRepository,times(1)).setAlarm(message1.getReciever(), AlarmEnum.CHAT);//any(User.class)
            verify(chatRoomRepository,times(1)).newMsg(message1.getRoomId(),message1.getWriter(),message1.getReciever(),message1.getType(),message1.getMessage());//any(User.class)
            verify(redisTemplate,times(1)).convertAndSend(eq("asdsagfasf"),eq(message1));//any(User.class)

        }
        @Test
        @DisplayName("채팅방에 2명있을때")
        void pub3() {
            String roomId = "abc";
            ChatMessageDTO message1 = new ChatMessageDTO();
            message1.setMessage("msg1");
            message1.setRoomId(roomId);
            message1.setWriter("a");
            message1.setReciever("reciever");
            ChatService chatService = new ChatService(redisTemplate,chatMessageRepository,chatRoomRepository,channelTopic);
            when(chatRoomRepository.getChatUserCnt(eq(roomId)))
                    .thenReturn(2);
            when(channelTopic.getTopic())
                    .thenReturn("asdsagfasf2");
            chatService.publish(message1,"sender");

            verify(chatRoomRepository,times(1)).newMsg(message1.getRoomId(),message1.getWriter(),message1.getReciever(),message1.getType(),message1.getMessage());//any(User.class)
            verify(redisTemplate,times(1)).convertAndSend(eq("asdsagfasf2"),eq(message1));//any(User.class)
        }
    }
    @Test
    @DisplayName("배치 배치 배치")
    void saveBuulk() {
        ChatService chatService = new ChatService(redisTemplate,chatMessageRepository,chatRoomRepository,channelTopic);
        chatService.saveBulk();
        verify(chatMessageRepository,times(1)).saveAll(anyList());//any(User.class)
    }

    @Test
    @DisplayName("userdisConnection")
    void userDisConnect() {
        String userName = "userName";
        String sessionId = "sessionId";
        ChatService chatService = new ChatService(redisTemplate,chatMessageRepository,chatRoomRepository,channelTopic);
        chatService.userDisConnect(userName,sessionId);
        verify(chatRoomRepository,times(1)).userDisConnect(userName,sessionId);
    }
    @Nested
    @DisplayName("User Connect")
    class Connect {
        @Test
        @DisplayName("로그인 연결")
        void login_Connect() {
            String dest = "/user";
            String sessionId= "sessionId";
            String userEmail= "userEmail";
            String sessionId2= "sessionId2";
            ChatService chatService = new ChatService(redisTemplate,chatMessageRepository,chatRoomRepository,channelTopic);
            chatService.userConnect(dest,sessionId,userEmail,sessionId2);
            verify(chatRoomRepository,times(1)).userConnect(userEmail,sessionId);
        }
        @Test
        @DisplayName("채팅방 입장")
        void chat_Connect() {
            String dest = "/sub/aaaaaaaaa/asdgggsad";
            String sessionId= "sessionId";
            String userEmail= "userEmail";
            String sessionId2= "sessionId2";
            ChatService chatService = new ChatService(redisTemplate,chatMessageRepository,chatRoomRepository,channelTopic);
            chatService.userConnect(dest,sessionId,userEmail,sessionId2);
            verify(chatRoomRepository,times(1)).enterRoom("asdgggsad",userEmail,sessionId2);

        }
    }


}
