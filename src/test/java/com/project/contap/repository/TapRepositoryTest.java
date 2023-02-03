package com.project.contap.repository;

import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TapRepositoryTest {

    @Autowired
    private TapRepository tapRepository;

    @Autowired
    private UserRepository userRepository;


    List<Long> userIds = new ArrayList<>();


    @BeforeEach
    void setUp() throws Exception {
        userIds = new ArrayList<>();
        User user1 = User.builder()
                .email("user1@gmail.com")
                .pw("123")
                .userName("user1")
                .field(0).build();
        User user2 = User.builder()
                .email("user2@gmail.com")
                .pw("123")
                .userName("user2")
                .field(1).build();
        User user3 = User.builder()
                .email("user3@gmail.com")
                .pw("123")
                .userName("user3")
                .field(1).build();
        User user4 = User.builder()
                .email("user4@gmail.com")
                .pw("123")
                .userName("user4")
                .field(1).build();

        userIds.add(userRepository.save(user1).getId());
        userIds.add(userRepository.save(user2).getId());
        userIds.add(userRepository.save(user3).getId());
        userIds.add(userRepository.save(user4).getId());

        Tap newTap1 = Tap.builder().sendUser(user1).receiveUser(user2).msg("gd~").status(0).build();
        Tap newTap2 = Tap.builder().sendUser(user1).receiveUser(user3).msg("gd~").status(0).build();
        Tap newTap3 = Tap.builder().sendUser(user1).receiveUser(user4).msg("gd~").status(0).build();
        Tap newTap4 = Tap.builder().sendUser(user3).receiveUser(user4).msg("gd~").status(0).build();

        tapRepository.save(newTap1);
        tapRepository.save(newTap2);
        tapRepository.save(newTap3);
        tapRepository.save(newTap4);
    }

    @Test
    @DisplayName("받은 탭 체크")
    void findByCategory01() {
        User user1 = userRepository.findById(userIds.get(0)).orElse(null);
        User user2 = userRepository.findById(userIds.get(1)).orElse(null);
        User user3 = userRepository.findById(userIds.get(2)).orElse(null);
        User user4 = userRepository.findById(userIds.get(3)).orElse(null);
        Tap tap1 = tapRepository.checkReceiveTap(user1,user2); //
        Tap tap2 = tapRepository.checkReceiveTap(user1,user3); //
        Tap tap3 = tapRepository.checkReceiveTap(user1,user4); //
        Tap tap4 = tapRepository.checkReceiveTap(user2,user1);
        Tap tap5 = tapRepository.checkReceiveTap(user2,user3);
        Tap tap6 = tapRepository.checkReceiveTap(user2,user4);
        Tap tap7 = tapRepository.checkReceiveTap(user3,user1);
        Tap tap8 = tapRepository.checkReceiveTap(user3,user2);
        Tap tap9 = tapRepository.checkReceiveTap(user3,user4); //
        Tap tap10 = tapRepository.checkReceiveTap(user4,user1);
        Tap tap11 = tapRepository.checkReceiveTap(user4,user2);
        Tap tap12 = tapRepository.checkReceiveTap(user4,user3);
        assertEquals(tap1.getReceiveUser().getId(),user2.getId());
        assertEquals(tap1.getSendUser().getId(),user1.getId());
        assertEquals(tap2.getReceiveUser().getId(),user3.getId());
        assertEquals(tap2.getSendUser().getId(),user1.getId());
        assertEquals(tap3.getReceiveUser().getId(),user4.getId());
        assertEquals(tap3.getSendUser().getId(),user1.getId());
        assertEquals(tap9.getReceiveUser().getId(),user4.getId());
        assertEquals(tap9.getSendUser().getId(),user3.getId());
        assertNull(tap4);
        assertNull(tap5);
        assertNull(tap6);
        assertNull(tap7);
        assertNull(tap8);
        assertNull(tap10);
        assertNull(tap11);
        assertNull(tap12);
    }

    @Test
    @DisplayName("보낸 탭 체크")
    void checksendtap() {
        User user1 = userRepository.findById(userIds.get(0)).orElse(null);
        User user2 = userRepository.findById(userIds.get(1)).orElse(null);
        User user3 = userRepository.findById(userIds.get(2)).orElse(null);
        User user4 = userRepository.findById(userIds.get(3)).orElse(null);

        Boolean tap1 = tapRepository.checkSendTap(user1,user2);
        Boolean tap2 = tapRepository.checkSendTap(user1,user3);
        Boolean tap3 = tapRepository.checkSendTap(user1,user4);

        Boolean tap4 = tapRepository.checkSendTap(user2,user1);//
        Boolean tap5 = tapRepository.checkSendTap(user2,user3);
        Boolean tap6 = tapRepository.checkSendTap(user2,user4);

        Boolean tap7 = tapRepository.checkSendTap(user3,user1);//
        Boolean tap8 = tapRepository.checkSendTap(user3,user2);
        Boolean tap9 = tapRepository.checkSendTap(user3,user4);

        Boolean tap10 = tapRepository.checkSendTap(user4,user1);//
        Boolean tap11 = tapRepository.checkSendTap(user4,user2);
        Boolean tap12 = tapRepository.checkSendTap(user4,user3);//

        assertTrue(tap4);
        assertTrue(tap7);
        assertTrue(tap10);
        assertTrue(tap12);
        assertFalse(tap1);
        assertFalse(tap2);
        assertFalse(tap3);
        assertFalse(tap5);
        assertFalse(tap6);
        assertFalse(tap8);
        assertFalse(tap9);
        assertFalse(tap11);
    }

    @Test
    @DisplayName("User 가 받거나 보낸 탭")
    void getMyTaps() {
        for(int i = 0 ; i < userIds.size() ; i++)
        {
            User user = userRepository.findById(userIds.get(i)).orElse(null);
            List<Tap> taps = tapRepository.getMyTaps(user);
            for(Tap tap : taps)
            {
                boolean check1 = tap.getReceiveUser().getId() == user.getId();
                boolean check2 = tap.getSendUser().getId() == user.getId();
                assertTrue(check1|check2);
            }
        }
    }

}