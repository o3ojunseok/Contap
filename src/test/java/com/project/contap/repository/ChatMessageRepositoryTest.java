package com.project.contap.repository;

import com.project.contap.common.util.RandomNumberGeneration;
import com.project.contap.model.chat.ChatMessage;
import com.project.contap.model.chat.ChatMessageRepository;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ChatMessageRepositoryTest {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

//    List<ChatMessage> findMessage(String roomId, Long startId);
//    ChatMessage findLastMessage(String roomId);
//    private Long id;
//    private String roomId;
//    private String message;
//    private String writer;
//    private LocalDateTime createdDt;

    List<String> rooms = new ArrayList<>();
    List<Long> ids = new ArrayList<>(); // 이건 가장최신것을 위한것이다.
    List<List<Long>> idsByrooms = new ArrayList<>();
    @BeforeEach
    void setUp() throws Exception {
        int testSize = 200;
        rooms.add("a");
        rooms.add("b");
        rooms.add("c");
        rooms.add("d");
        for(int i = 0 ; i <testSize; i++)
        {
            int roomidx = i%4;
            ChatMessage newMsg = new ChatMessage();
            newMsg.setMessage(String.format("%d",i).toString());
            newMsg.setCreatedDt(LocalDateTime.now());
            newMsg.setWriter("sender");
            newMsg.setRoomId(rooms.get(roomidx));
            Long id = chatMessageRepository.save(newMsg).getId();
            if(roomidx == idsByrooms.size()) {
                List<Long>  imsi =  new ArrayList<>();
                imsi.add(id);
                idsByrooms.add(imsi);
            }
            else
            {
                idsByrooms.get(roomidx).add(id);
            }
            if(i >= testSize - rooms.size())
                ids.add(id);
        }
        System.out.println("asdas");
    }

    @Test
    @DisplayName("가장 최근 MSG")
    void lastMsg() {
        for(int i = 0 ; i < rooms.size();i++)
            assertEquals(ids.get(i),chatMessageRepository.findLastMessage(rooms.get(i)).getId());
    }

    @Test
    @DisplayName("채팅방에서 MSG 페이징 처리로 읽어올때")
    void get_Msg_Page() {
        for(int i = 1 ; i <= rooms.size();i++)
        {
            int idx = RandomNumberGeneration.randomRange(30,45);
            int real = (idx-1) * rooms.size()+i;
            int index = idsByrooms.get(i-1).indexOf(new Long(real))-1;
            List<ChatMessage> msgs = chatMessageRepository.findMessage(rooms.get(i-1),new Long(real));
            System.out.println(msgs);
            for(int j = 0 ; j < msgs.size();j++)
            {
                assertEquals(idsByrooms.get(i-1).get(index),msgs.get(j).getId());
                index = index-1;
            }
        }
    }

    @Test
    @DisplayName("채팅방 최초 입장시 MSG  얻어올때")
    void get_Msg_Enter() {
        for(int i = 0 ; i < rooms.size();i++)
        {
            int length = idsByrooms.get(i).size() - 1;
            List<ChatMessage> msgs = chatMessageRepository.findMessage(rooms.get(i),0L);
            System.out.println(msgs);
            for(int j = 0 ; j < msgs.size();j++)
            {
                assertEquals(idsByrooms.get(i).get(length),msgs.get(j).getId());
                length = length-1;
            }
        }
    }
}
