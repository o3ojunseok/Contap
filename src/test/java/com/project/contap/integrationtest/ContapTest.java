package com.project.contap.integrationtest;

import com.project.contap.model.chat.ChatRoomRepository;
import com.project.contap.common.DefaultRsp;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.service.ContapService;
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
public class ContapTest {
    @Autowired
    ContapService contapService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FriendRepository friendRepository;
    @Autowired
    TapRepository tapRepository;
    @Autowired
    ChatRoomRepository chatRoomRepository;

    List<User> users = new ArrayList<>();
    List<Tap> taps = new ArrayList<>();
    Long notfTap = 99999L;
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
        taps.add(tapRepository.save(newTap));

        Tap newTap2 = Tap.builder()
                .sendUser(users.get(3))
                .receiveUser(users.get(0))
                .msg("msg")
                .build();
        taps.add(tapRepository.save(newTap2));

        Tap newTap3 = Tap.builder()
                .sendUser(users.get(0))
                .receiveUser(users.get(1))
                .msg("msg")
                .status(2)
                .build();
        taps.add(tapRepository.save(newTap3));
    }


    @Test
    @DisplayName("탭수락 - 정상처리")
    void rapAccept_good() {
        DefaultRsp dfRsp = contapService.tapReject(taps.get(0).getId(),users.get(2).getEmail());
        assertEquals("정상적으로 처리 되었습니다.", dfRsp.getMsg());
    }

    @Test
    @DisplayName("탭수락 - 이미 처리된 탭")
    void rapAccept_already() {
        DefaultRsp dfRsp = contapService.tapReject(taps.get(2).getId(),users.get(1).getEmail());
        assertEquals("이미 처리된 Tap 입니다.", dfRsp.getMsg());
    }

    @Test
    @DisplayName("탭수락 - 찾을수없음")
    void rapAccept_notf() {
        DefaultRsp dfRsp = contapService.tapReject(notfTap,users.get(2).getEmail());
        assertEquals("해당 tab이 존재하지 않습니다 TabID를 다시확인해주세요..", dfRsp.getMsg());
    }
    @Test
    @DisplayName("탭거절 - 정상처리")
    void tapReject_good() {
        DefaultRsp dfRsp = contapService.tapReject(taps.get(1).getId(),users.get(0).getEmail());
        assertEquals("정상적으로 처리 되었습니다.", dfRsp.getMsg());
    }

    @Test
    @DisplayName("탭거절 - 이미 처리")
    void tapReject_already() {
        DefaultRsp dfRsp = contapService.tapReject(taps.get(2).getId(),users.get(1).getEmail());
        assertEquals("이미 처리된 Tap 입니다.", dfRsp.getMsg());
    }

    @Test
    @DisplayName("탭거절 - 찾을수없음")
    void tapReject_notf() {
        DefaultRsp dfRsp = contapService.tapReject(notfTap,users.get(0).getEmail());
        assertEquals("해당 tab이 존재하지 않습니다 TabID를 다시확인해주세요..", dfRsp.getMsg());
    }
}
