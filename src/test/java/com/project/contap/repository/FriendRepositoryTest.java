package com.project.contap.repository;

import com.project.contap.common.Common;
import com.project.contap.model.card.Card;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.card.dto.QCardDto;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.FriendRepository;
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
class FriendRepositoryTest {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;


    List<Long> userIds = new ArrayList<>();
    List<Long> friendIds = new ArrayList<>();


    @BeforeEach
    void setUp() throws Exception {

        userIds = new ArrayList<>();
        friendIds = new ArrayList<>();
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

        Friend fir = Friend.builder()
                .roomId("roomId")
                .me(user1)
                .you(user2)
                .build();
        Friend sec = Friend.builder()
                .roomId("roomId")
                .me(user2)
                .you(user1)
                .build();
        friendIds.add(friendRepository.save(fir).getId());
        friendIds.add(friendRepository.save(sec).getId());

        Friend fir1 = Friend.builder()
                .roomId("roomId")
                .me(user1)
                .you(user3)
                .build();
        Friend sec1 = Friend.builder()
                .roomId("roomId")
                .me(user3)
                .you(user1)
                .build();
        friendIds.add(friendRepository.save(fir1).getId());
        friendIds.add(friendRepository.save(sec1).getId());

        Friend fir2 = Friend.builder()
                .roomId("roomId")
                .me(user1)
                .you(user4)
                .build();
        Friend sec2 = Friend.builder()
                .roomId("roomId")
                .me(user4)
                .you(user1)
                .build();
        friendIds.add(friendRepository.save(fir2).getId());
        friendIds.add(friendRepository.save(sec2).getId());

        Friend fir3 = Friend.builder()
                .roomId("roomId")
                .me(user3)
                .you(user4)
                .build();
        Friend sec3 = Friend.builder()
                .roomId("roomId")
                .me(user4)
                .you(user3)
                .build();
        friendIds.add(friendRepository.save(fir3).getId());
        friendIds.add(friendRepository.save(sec3).getId());
    }

    @Test
    @DisplayName("친구관계 체크")
    void checkFriend() {
        User user1 = userRepository.findById(userIds.get(0)).orElse(null);
        User user2 = userRepository.findById(userIds.get(1)).orElse(null);
        User user3 = userRepository.findById(userIds.get(2)).orElse(null);
        User user4 = userRepository.findById(userIds.get(3)).orElse(null);
        assertTrue(friendRepository.checkFriend(user1,user2));
        assertTrue(friendRepository.checkFriend(user1,user3));
        assertTrue(friendRepository.checkFriend(user1,user4));
        assertTrue(friendRepository.checkFriend(user3,user4));
        assertTrue(friendRepository.checkFriend(user4,user3));
        assertTrue(friendRepository.checkFriend(user4,user1));
        assertTrue(friendRepository.checkFriend(user3,user1));
        assertTrue(friendRepository.checkFriend(user2,user1));
        assertFalse(friendRepository.checkFriend(user2,user3));
        assertFalse(friendRepository.checkFriend(user2,user4));
        assertFalse(friendRepository.checkFriend(user3,user2));
        assertFalse(friendRepository.checkFriend(user4,user2));
    }

    @Test
    @DisplayName("Friend 엔티티 불러오기")
    void getFriend() {
        User user1 = userRepository.findById(userIds.get(0)).orElse(null);
        User user2 = userRepository.findById(userIds.get(1)).orElse(null);
        User user3 = userRepository.findById(userIds.get(2)).orElse(null);
        User user4 = userRepository.findById(userIds.get(3)).orElse(null);
        assertNotNull(friendRepository.getFriend(user1,user2));
        assertNotNull(friendRepository.getFriend(user1,user3));
        assertNotNull(friendRepository.getFriend(user1,user4));
        assertNotNull(friendRepository.getFriend(user2,user1));
        assertNull(friendRepository.getFriend(user2,user3));
        assertNull(friendRepository.getFriend(user2,user4));
        assertNotNull(friendRepository.getFriend(user3,user1));
        assertNull(friendRepository.getFriend(user3,user2));
        assertNotNull(friendRepository.getFriend(user3,user4));
        assertNotNull(friendRepository.getFriend(user4,user1));
        assertNull(friendRepository.getFriend(user4,user2));
        assertNotNull(friendRepository.getFriend(user4,user3));
    }

    @Test
    @DisplayName("User 와 관련된 Friend 엔티티 모두다 불러오기")
    void getallmyFriend() {
        for(int i = 0;i < userIds.size();i++)
        {
            User user = userRepository.findById(userIds.get(i)).orElse(null);
            List<Friend> friends = friendRepository.getallmyFriend(user);
            for(Friend friend : friends)
            {
                boolean check1 = friend.getMe().getId() == user.getId();
                boolean check2 = friend.getYou().getId() == user.getId();
                assertTrue(check1|check2);
            }
        }
    }
}