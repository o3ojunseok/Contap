package com.project.contap.common;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.project.contap.common.enumlist.AuthorityEnum;
import com.project.contap.common.enumlist.MsgTypeEnum;
import com.project.contap.model.chat.ChatMessageDTO;
import com.project.contap.model.chat.ChatMessageRepository;
import com.project.contap.model.chat.ChatRoomRepository;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.user.User;
import com.project.contap.service.EmailServiceTest;
import com.project.contap.service.MypageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommonTest {
    @InjectMocks
    private Common common;

    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private RedisTemplate redisTemplate;
    @Mock
    private ChannelTopic channelTopic;
    @Mock
    private FriendRepository friendRepository;
    @Mock
    private ChatMessageRepository chatMessageRepository;


    @Test
    @DisplayName("msgDto Set")
    void setChatMessageDTO() {
        ChatMessageDTO ret = common.setChatMessageDTO(MsgTypeEnum.SEND_TAP,"receiver","sender","session");
        assertEquals(MsgTypeEnum.SEND_TAP.getValue(),ret.getType());
        assertEquals("receiver",ret.getReciever());
        assertEquals("sender",ret.getWriter());
        assertEquals("session",ret.getSessionId());
        assertEquals(MsgTypeEnum.SEND_TAP.getMsg(),ret.getMessage());
    }

    @Test
    @DisplayName("친구 만들기")
    void makeChatRoom() {
        User user1 = User.builder().id(1L).email("email1").build();
        User user2 = User.builder().id(2L).email("email2").build();
        common.makeChatRoom(user1,user2);
        verify(friendRepository,times(2)).save(any(Friend.class));
        verify(chatRoomRepository,times(1)).whenMakeFriend(any(String.class),eq(user1.getEmail()),eq(user2.getEmail()));

    }
    @Nested
    @DisplayName("알람기능")
    class Alarm{
        @Test
        @DisplayName("유저 로그인 중일때 - 탭요청")
        void Login() {

            String tapSender = "email1";
            String tapReceiver = "email2";
            User user = User.builder().id(1L).email("email1").build();
            MsgTypeEnum type = MsgTypeEnum.SEND_TAP;
            String receiverssesion = "sessionId";
            when(chatRoomRepository.getSessionId(tapReceiver)).thenReturn("receiverssesion");
            when(channelTopic.getTopic()).thenReturn("Topic");
            common.sendAlarmIfneeded(type,tapSender,tapReceiver,user);
            verify(redisTemplate,times(1)).convertAndSend(eq("Topic"),any(ChatMessageDTO.class));
        }
        @Test
        @DisplayName("유저 로그아웃 중일때 - 탭요청 - 폰 번호 없음")
        void Logoff1() {
            String email1 = "email1";
            String email2 = "email2";
            User user1 = User.builder().id(1L).email("email1").build();
            when(chatRoomRepository.getSessionId(email2)).thenReturn(null);
            common.sendAlarmIfneeded(MsgTypeEnum.SEND_TAP,email1,email2,user1);
            verify(chatRoomRepository,times(1)).setAlarm(email2,MsgTypeEnum.SEND_TAP.getAlarmEnum());
        }
        @Test
        @DisplayName("유저 로그아웃 중일때 - 탭요청 - 폰 번호 있지만 알람꺼둠")
        void Logoff2() {
            String email1 = "email1";
            String email2 = "email2";
            User user1 = User.builder().phoneNumber("01066454534").authStatus(0).id(1L).email("email1").build();
            when(chatRoomRepository.getSessionId(email2)).thenReturn(null);
            common.sendAlarmIfneeded(MsgTypeEnum.SEND_TAP,email1,email2,user1);
            verify(chatRoomRepository,times(1)).setAlarm(email2,MsgTypeEnum.SEND_TAP.getAlarmEnum());
        }
        @Test
        @DisplayName("유저 로그아웃 중일때 - 탭요청")
        void Logoff3() {
            String email1 = "email1";
            String email2 = "email2";
            User user1 = User.builder().phoneNumber("01066454534").authStatus(AuthorityEnum.ALARM.getAuthority()).id(1L).email("email1").build();
            when(chatRoomRepository.getSessionId(email2)).thenReturn(null);
            common.sendAlarmIfneeded(MsgTypeEnum.SEND_TAP,email1,email2,user1);
            verify(chatRoomRepository,times(1)).setAlarm(email2,MsgTypeEnum.SEND_TAP.getAlarmEnum());
        }

    }

}
