package com.project.contap.integrationtest;

import com.project.contap.model.chat.ChatRoomRepository;
import com.project.contap.common.DefaultRsp;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.service.MainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class MainTest {
    @Autowired
    MainService mainService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FriendRepository friendRepository;
    @Autowired
    TapRepository tapRepository;
    @Autowired
    ChatRoomRepository chatRoomRepository;

    List<User> users = new ArrayList<>();
    @BeforeEach
    void SetUp() {
        for(int i = 0 ; i<5 ; i++)
        {
            User user1 = User.builder()
                    .email(String.format("testUser%d@gmail.com",i))
                    .pw("123")
                    .userName(String.format("user%d",i))
                    .field(0).build();
            users.add(userRepository.save(user1));
        }
        Friend fir = Friend.builder()
                .roomId("roomId")
                .me(users.get(0))
                .you(users.get(1))
                .build();
        Friend sec = Friend.builder()
                .roomId("roomId")
                .me(users.get(1))
                .you(users.get(0))
                .build();
        friendRepository.save(fir);
        friendRepository.save(sec);

        Tap newTap = Tap.builder()
                .sendUser(users.get(0))
                .receiveUser(users.get(2))
                .msg("msg")
                .build();
        tapRepository.save(newTap);

        Tap newTap2 = Tap.builder()
                .sendUser(users.get(3))
                .receiveUser(users.get(0))
                .msg("msg")
                .build();
        tapRepository.save(newTap2);
    }

    @Test
    @DisplayName("탭요청 - 정상처리")
    void dotap_good() {
        DefaultRsp dfRsp = mainService.dotap(users.get(0),users.get(4).getId(),"gd");
        assertEquals("정상적으로 처리 되었습니다.", dfRsp.getMsg());
    }

    @Test
    @DisplayName("탭요청 - 자신한테 탭요청")
    void dotap_sendto() {
        DefaultRsp dfRsp = mainService.dotap(users.get(0),users.get(0).getId(),"gd");
        assertEquals("자신한테 탭요청하지못해요", dfRsp.getMsg());
    }

    @Test
    @DisplayName("탭요청 - 이미 친구 관계1")
    void dotap_alreadyfriend_1() {
        DefaultRsp dfRsp = mainService.dotap(users.get(0),users.get(1).getId(),"gd");
        assertEquals("이미 친구 관계입니다.", dfRsp.getMsg());
    }
    @Test
    @DisplayName("탭요청 - 이미 친구 관계2")
    void dotap_alreadyfriend_2() {
        DefaultRsp dfRsp = mainService.dotap(users.get(1),users.get(0).getId(),"gd");
        assertEquals("이미 친구 관계입니다.", dfRsp.getMsg());
    }

    @Test
    @DisplayName("탭요청 - 이미 상대한테 요청을 보낸 상태")
    void dotap_alreadysend() {
        DefaultRsp dfRsp = mainService.dotap(users.get(0),users.get(2).getId(),"gd");
        assertEquals("이미 상대에게 요청을 보낸 상태입니다.", dfRsp.getMsg());
    }

    @Test
    @DisplayName("탭요청 - 상대도 나한테 요청을 보낸 상태")
    void dotap_receiversendme() {
        DefaultRsp dfRsp = mainService.dotap(users.get(0),users.get(3).getId(),"gd");
        assertEquals("상대방의 요청을 수락 하였습니다.", dfRsp.getMsg());
    }
}
