package com.project.contap.service;

import com.project.contap.common.Common;
import com.project.contap.common.DefaultRsp;
import com.project.contap.common.SearchRequestDto;
import com.project.contap.common.enumlist.AuthorityEnum;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.hashtag.HashTagRepositoty;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.security.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainServiceTest {

    @Mock
    HashTagRepositoty hashTagRepositoty;
    @Mock
    UserRepository userRepository;
    @Mock
    TapRepository tapRepository;
    @Mock
    FriendRepository friendRepository;
    @Mock
    CardRepository cardRepository;
    @Mock
    UserService userService;
    @Mock
    Common common;

    Long tapId = 1L;
    String receiveUserEmail = "lsj2867@gmail.com";
    User imsiSender = User.builder()
            .id(1L)
            .email("lsj2867@gmail.com")
            .pw("123")
            .userName("lsj2867")
            .field(0).build();
    User imsiReceiver = User.builder()
            .id(2L)
            .email("lsj4534@gmail.com")
            .pw("123")
            .userName("lsj4534")
            .field(1).build();


    @Nested
    @DisplayName("탭요청")
    class postTap {
        @Test
        @DisplayName("정상처리")
        void dotap_good() {
            Tap mockTap = null;
            MainService mainService = new MainService(hashTagRepositoty, userRepository, tapRepository, friendRepository, cardRepository, userService, common);
            when(userRepository.findById(imsiReceiver.getId()))
                    .thenReturn(Optional.of(imsiReceiver));
            when(friendRepository.checkFriend(imsiSender, imsiReceiver))
                    .thenReturn(false);
            when(tapRepository.checkReceiveTap(imsiReceiver, imsiSender))
                    .thenReturn(mockTap);
            when(tapRepository.checkSendTap(imsiReceiver, imsiSender))
                    .thenReturn(false);
            DefaultRsp dfRsp = mainService.dotap(imsiSender, imsiReceiver.getId(), "gd");
            assertEquals("정상적으로 처리 되었습니다.", dfRsp.getMsg());
        }

        @Test
        @DisplayName("자신한테 탭요청")
        void dotap_sendto() {
            MainService mainService = new MainService(hashTagRepositoty, userRepository, tapRepository, friendRepository, cardRepository, userService, common);
            DefaultRsp dfRsp = mainService.dotap(imsiSender, imsiSender.getId(), "gd");
            assertEquals("자신한테 탭요청하지못해요", dfRsp.getMsg());
        }

        @Test
        @DisplayName("이미 친구 관계")
        void dotap_alreadyfriend() {
            MainService mainService = new MainService(hashTagRepositoty, userRepository, tapRepository, friendRepository, cardRepository, userService, common);
            when(userRepository.findById(imsiReceiver.getId()))
                    .thenReturn(Optional.of(imsiReceiver));
            when(friendRepository.checkFriend(imsiSender, imsiReceiver))
                    .thenReturn(true);
            DefaultRsp dfRsp = mainService.dotap(imsiSender, imsiReceiver.getId(), "gd");
            assertEquals("이미 친구 관계입니다.", dfRsp.getMsg());
        }

        @Test
        @DisplayName("이미 상대한테 요청을 보낸 상태")
        void dotap_alreadysend() {
//        Tap mockTap = Tap.builder().id(1L).sendUser(imsiSender).receiveUser(imsiReceiver).msg("gd").status(0).build();
            Tap mockTap = null;
            MainService mainService = new MainService(hashTagRepositoty, userRepository, tapRepository, friendRepository, cardRepository, userService, common);
            when(userRepository.findById(imsiReceiver.getId()))
                    .thenReturn(Optional.of(imsiReceiver));
            when(friendRepository.checkFriend(imsiSender, imsiReceiver))
                    .thenReturn(false);
            when(tapRepository.checkReceiveTap(imsiReceiver, imsiSender))
                    .thenReturn(mockTap);
            when(tapRepository.checkSendTap(imsiReceiver, imsiSender))
                    .thenReturn(true);
            DefaultRsp dfRsp = mainService.dotap(imsiSender, imsiReceiver.getId(), "gd");
            assertEquals("이미 상대에게 요청을 보낸 상태입니다.", dfRsp.getMsg());
        }

        @Test
        @DisplayName("상대도 나한테 요청을 보낸 상태")
        void dotap_receiversendme() {
            Tap mockTap = Tap.builder().id(1L).sendUser(imsiSender).receiveUser(imsiReceiver).msg("gd").status(0).build();
            MainService mainService = new MainService(hashTagRepositoty, userRepository, tapRepository, friendRepository, cardRepository, userService, common);
            when(userRepository.findById(imsiReceiver.getId()))
                    .thenReturn(Optional.of(imsiReceiver));
            when(friendRepository.checkFriend(imsiSender, imsiReceiver))
                    .thenReturn(false);
            when(tapRepository.checkReceiveTap(imsiReceiver, imsiSender))
                    .thenReturn(mockTap);
            DefaultRsp dfRsp = mainService.dotap(imsiSender, imsiReceiver.getId(), "gd");
            assertEquals("상대방의 요청을 수락 하였습니다.", dfRsp.getMsg());
        }
    }

    @Nested
    @DisplayName("튜토리얼 비트 Set")
    class tutorial {
        @Test
        @DisplayName("Type 0")
        void tutorial_type0() {
            User user = User.builder().id(999999L).email("user").pw("pw").userName("userName").authStatus(0).build();
            MainService mainService = new MainService(hashTagRepositoty, userRepository, tapRepository, friendRepository, cardRepository, userService, common);
            when(userRepository.save(user))
                    .thenReturn(user);
            mainService.tutorial(0,user);
            assertEquals(user.getAuthStatus()&AuthorityEnum.PHONE_TUTORIAL.getAuthority(),AuthorityEnum.PHONE_TUTORIAL.getAuthority());
        }
        @Test
        @DisplayName("Type 1")
        void tutorial_type1() {
            User user = User.builder().id(999999L).email("user").pw("pw").userName("userName").authStatus(0).build();
            MainService mainService = new MainService(hashTagRepositoty, userRepository, tapRepository, friendRepository, cardRepository, userService, common);
            when(userRepository.save(user))
                    .thenReturn(user);
            mainService.tutorial(1,user);
            assertEquals(user.getAuthStatus()&AuthorityEnum.PROFILE_TUTORIAL.getAuthority(),AuthorityEnum.PROFILE_TUTORIAL.getAuthority());
        }
    }
    @Test
    @DisplayName("get HashTag")
    void HashTag() {
        MainService mainService = new MainService(hashTagRepositoty, userRepository, tapRepository, friendRepository, cardRepository, userService, common);
        mainService.getHashTag();
        verify(hashTagRepositoty,times(1)).findAll();
    }
    @Test
    @DisplayName("Search")
    void Search() {
        SearchRequestDto dto = new SearchRequestDto();
        MainService mainService = new MainService(hashTagRepositoty, userRepository, tapRepository, friendRepository, cardRepository, userService, common);
        mainService.searchuser(dto,0L);
        verify(userRepository,times(1)).findAllByTag(dto,0L);
    }
    @Test
    @DisplayName("get Cards")
    void Cards() {
        Long userId = 1L;
        MainService mainService = new MainService(hashTagRepositoty, userRepository, tapRepository, friendRepository, cardRepository, userService, common);
        mainService.getCards(userId);
        verify(cardRepository,times(1)).findAllByUserId(userId);
    }
    @Test
    @DisplayName("get Main")
    void Main() {
        UserDetailsImpl testUserDetails;
        User testUser = User.builder().id(9999L).email("testuser@gmail.com").userName("testuser").pw("ab").field(0).build();
        testUserDetails = new UserDetailsImpl(testUser);
        MainService mainService = new MainService(hashTagRepositoty, userRepository, tapRepository, friendRepository, cardRepository, userService, common);
        mainService.getUserDtoList(testUserDetails);
        verify(userRepository,times(1)).getRandomUser(any(Long.class),eq(testUser.getId()));

    }
    @Test
    @DisplayName("get Userauth status")
    void Userauth() {
        User testUser = User.builder().id(9999L).email("testuser@gmail.com").userName("testuser").pw("ab").field(0).build();
        MainService mainService = new MainService(hashTagRepositoty, userRepository, tapRepository, friendRepository, cardRepository, userService, common);
        mainService.getUserAuthStatus(testUser);
    }
}