package com.project.contap.repository;

import com.project.contap.common.enumlist.AlarmEnum;
import com.project.contap.model.chat.ChatMessage;
import com.project.contap.model.chat.ChatRoomRepository;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ChatRoomRepositoryTest {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    @DisplayName("유저 로그인 연결 및 연결 해제")
    void UserConnection() {
        String email = "lsj2867@gmail.com";
        String userName = UUID.randomUUID().toString();
        String sessionId = UUID.randomUUID().toString();

        chatRoomRepository.userConnect(email,userName);
        assertEquals(userName,chatRoomRepository.getSessionId(email));
        chatRoomRepository.userDisConnect(userName,sessionId);
        assertNull(chatRoomRepository.getSessionId(email));

    }

    @Test
    @DisplayName("유저 삭제")
    void deleteUser() {
        String email = "lsj2867@gmail.com";
        String userName = UUID.randomUUID().toString();
        String sessionId = UUID.randomUUID().toString();

        chatRoomRepository.userConnect(email,userName);
        assertEquals(userName,chatRoomRepository.getSessionId(email));
        chatRoomRepository.setAlarm(email, AlarmEnum.TAP_RECEIVE);
        String[] alarm = chatRoomRepository.readAlarm(email);
        assertNotEquals("0,0,0,0",String.join(",",alarm));
        alarm = chatRoomRepository.readAlarm(email);
        assertEquals("0,0,0,0",String.join(",",alarm));

        chatRoomRepository.setAlarm(email, AlarmEnum.TAP_RECEIVE);
        chatRoomRepository.deleteUser(email);
        alarm = chatRoomRepository.readAlarm(email);
        assertEquals("0,0,0,0",String.join(",",alarm));
        assertNull(chatRoomRepository.getSessionId(email));

    }

    @Nested
    @DisplayName("roomStatus , sortData")
    class roomStatus {
        @Test
        @DisplayName("채팅방 만들기 방 status")
        void aboutRoomTest_init() throws InterruptedException {
            String emailMe = "alsj2867@gmail.com";
            String emailYou = "blsj2867@gmail.com";
            String roomId = UUID.randomUUID().toString();
            chatRoomRepository.whenMakeFriend(roomId,emailMe,emailYou);
            assertEquals("@@/0/_test용_",chatRoomRepository.getRoomStatus(roomId));
            Thread.sleep(2000);
            String emailYou2 = "dlsj2867@gmail.com";
            String roomId2 = UUID.randomUUID().toString();
            chatRoomRepository.whenMakeFriend(roomId2,emailMe,emailYou2);
            assertEquals("@@/0/_test용_",chatRoomRepository.getRoomStatus(roomId2));

            List<List<String>> order = chatRoomRepository.getMyFriendsOrderByDate(0,emailMe,0);
            assertEquals(roomId,order.get(0).get(1));
            assertEquals(roomId2,order.get(0).get(0));

            chatRoomRepository.whendeleteFriend(roomId,emailMe,emailYou);
            chatRoomRepository.whendeleteFriend(roomId2,emailMe,emailYou2);
            assertNull(chatRoomRepository.getRoomStatus(roomId));
            assertNull(chatRoomRepository.getRoomStatus(roomId2));


        }

        @Test
        @DisplayName("메시지 관련, 입장 퇴장")
        void aboutRoomTest_msg_leaveandenter() throws InterruptedException {
            String emailMe = "alsj2867@gmail.com";
            String emailYou = "blsj2867@gmail.com";
            String roomId = UUID.randomUUID().toString();
            chatRoomRepository.whenMakeFriend(roomId,emailMe,emailYou);
            assertEquals("@@/0/_test용_",chatRoomRepository.getRoomStatus(roomId));
            assertEquals(0,chatRoomRepository.getChatUserCnt(roomId));
            String name_me = UUID.randomUUID().toString();
            String name_you = UUID.randomUUID().toString();
            chatRoomRepository.enterRoom(roomId,emailMe,name_me);
            assertEquals("@@/1/_test용_",chatRoomRepository.getRoomStatus(roomId));
            assertEquals(1,chatRoomRepository.getChatUserCnt(roomId));
            chatRoomRepository.newMsg(roomId,emailMe,emailYou,1,"gd");
            assertEquals(emailMe+"/1/gd",chatRoomRepository.getRoomStatus(roomId));
            chatRoomRepository.enterRoom(roomId,emailYou,name_you);
            assertEquals("@@/2/gd",chatRoomRepository.getRoomStatus(roomId));
            assertEquals(2,chatRoomRepository.getChatUserCnt(roomId));
            chatRoomRepository.newMsg(roomId,emailMe,emailYou,0,"gd123");
            assertEquals("@@/2/gd123",chatRoomRepository.getRoomStatus(roomId));
            chatRoomRepository.leaveRoom(roomId);
            assertEquals(1,chatRoomRepository.getChatUserCnt(roomId));
            assertEquals("@@/1/gd123",chatRoomRepository.getRoomStatus(roomId));
            chatRoomRepository.leaveRoom(roomId);
            chatRoomRepository.whendeleteFriend(roomId,emailMe,emailYou);

            chatRoomRepository.userDisConnect("abc",name_me);
            chatRoomRepository.userDisConnect("abc",name_you);
        }
    }

    @Nested
    @DisplayName("roomStatus , sortData")
    class dbToRedis
    {
        @Test
        @DisplayName("주고받은 대화 있는경우")
        void msg() {
            User me = User.builder().email("lsj@gmail.com").build();
            User you = User.builder().email("lsj2@gmail.com").build();
            String roomId = UUID.randomUUID().toString();
            Friend friend = Friend.builder().me(me).you(you).roomId(roomId).build();
            ChatMessage msg = new ChatMessage();
            msg.setRoomId(roomId);
            msg.setMessage("TTEESSTT");
            msg.setCreatedDt(LocalDateTime.now());
            msg.setWriter(me.getEmail());
            chatRoomRepository.setDBinfoToRedis(friend,msg);
            assertEquals(me.getEmail()+"/0/"+msg.getMessage(),chatRoomRepository.getRoomStatus(roomId));
            chatRoomRepository.whendeleteFriend(roomId,me.getEmail(),you.getEmail());
        }

        @Test
        @DisplayName("주고받은 대화 없는경우")
        void nomsg() {
            User me = User.builder().email("lsj@gmail.com").build();
            User you = User.builder().email("lsj2@gmail.com").build();
            String roomId = UUID.randomUUID().toString();
            Friend friend = Friend.builder().me(me).you(you).roomId(roomId).build();
            ChatMessage msg = null;
            chatRoomRepository.setDBinfoToRedis(friend,msg);
            assertEquals("@@/0/_test용_",chatRoomRepository.getRoomStatus(roomId));
            chatRoomRepository.whendeleteFriend(roomId,me.getEmail(),you.getEmail());
        }
    }
}
