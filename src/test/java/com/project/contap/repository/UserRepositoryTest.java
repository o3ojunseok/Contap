package com.project.contap.repository;

import com.project.contap.common.SearchRequestDto;
import com.project.contap.common.enumlist.UserStatusEnum;
import com.project.contap.common.util.RandomNumberGeneration;
import com.project.contap.model.card.Card;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.hashtag.HashTag;
import com.project.contap.model.hashtag.HashTagRepositoty;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.model.user.dto.UserMainDto;
import com.project.contap.model.user.dto.UserTapDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TapRepository tapRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private HashTagRepositoty hashTagRepositoty;
    @Autowired
    private CardRepository cardRepository;



    List<User> users = new ArrayList<>();
    List<Tap> recieveTaps = new ArrayList<>();
    List<Tap> sendTaps = new ArrayList<>();
    List<Friend> friends = new ArrayList<>();
    List<String> roomids = new ArrayList<>();
    List<HashTag> hashTags = new ArrayList<>();
    Long userCount = 0L;

    @BeforeEach
    void setUp() throws Exception {
        userCount = 0L;

        for(int i = 0 ; i < 40 ; i++)
        {
            HashTag  newtag = new HashTag(String.format("name%d",i),0);
            hashTags.add(hashTagRepositoty.save(newtag));
        }
        for(int i = 0 ; i < 100 ; i++)
        {
            int i1 = RandomNumberGeneration.randomRange(1,15);
            int i2 = RandomNumberGeneration.randomRange(16,30);
            HashTag  tag1 = hashTagRepositoty.findById(hashTags.get(i1).getId()).orElse(null);
            HashTag  tag2 = hashTagRepositoty.findById(hashTags.get(i2).getId()).orElse(null);
            List<HashTag> userTags = new ArrayList<>();
            userTags.add(tag1);
            userTags.add(tag2);
            String tagString = "@"+tag1.getName()+"@"+tag2.getName()+"@_@";
            User user123 = User.builder()
                    .email(String.format("user%d@gmail.com",i))
                    .pw("123")
                    .userName(String.format("username%d.com",i))
                    .profile(String.format("userprofile%d.com",i))
                    .hashTagsString(tagString)
                    .tags(userTags)
                    .phoneNumber(String.format("01011112222%d",i))
                    .userStatus(UserStatusEnum.ACTIVE)
                    .field(0).build();
            users.add(userRepository.save(user123));
            userCount = userCount + 1;
        }
        for(int i = 0 ; i < 100 ; i ++)
        {
            Card card = Card.builder().user(users.get(i)).build();
            cardRepository.save(card);
        }

        for(int i = 100 ; i < 110 ; i ++) {
            int i1 = RandomNumberGeneration.randomRange(1,15);
            int i2 = RandomNumberGeneration.randomRange(16,30);
            HashTag  tag1 = hashTagRepositoty.findById(hashTags.get(i1).getId()).orElse(null);
            HashTag  tag2 = hashTagRepositoty.findById(hashTags.get(i2).getId()).orElse(null);
            List<HashTag> userTags = new ArrayList<>();
            userTags.add(tag1);
            userTags.add(tag2);
            String tagString = "@"+tag1.getName()+"@"+tag2.getName()+"@_@";
            User user123 = User.builder()
                    .email(String.format("user%d@gmail.com",i))
                    .pw("123")
                    .userName(String.format("username%d.com",i))
                    .profile(String.format("userprofile%d.com",i))
                    .hashTagsString(tagString)
                    .tags(userTags)
                    .phoneNumber(String.format("01011112222%d",i))
                    .userStatus(UserStatusEnum.INACTIVE)
                    .field(0).build();
            users.add(userRepository.save(user123));
            Card card = Card.builder().user(users.get(users.size()-1)).build();
            cardRepository.save(card);
        }
        for(int i = 110 ; i < 120 ; i ++) {
            User user123 = User.builder()
                    .email(String.format("user%d@gmail.com",i))
                    .pw("123")
                    .userName(String.format("username%d.com",i))
                    .profile(String.format("userprofile%d.com",i))
                    .hashTagsString("@_@")
                    .phoneNumber(String.format("01011112222%d",i))
                    .userStatus(UserStatusEnum.ACTIVE)
                    .field(0).build();
            users.add(userRepository.save(user123));
            Card card = Card.builder().user(users.get(users.size()-1)).build();
            cardRepository.save(card);
        }
        for(int i = 120 ; i < 130 ; i ++) {
            int i1 = RandomNumberGeneration.randomRange(1,15);
            int i2 = RandomNumberGeneration.randomRange(16,30);
            HashTag  tag1 = hashTagRepositoty.findById(hashTags.get(i1).getId()).orElse(null);
            HashTag  tag2 = hashTagRepositoty.findById(hashTags.get(i2).getId()).orElse(null);
            List<HashTag> userTags = new ArrayList<>();
            userTags.add(tag1);
            userTags.add(tag2);
            String tagString = "@"+tag1.getName()+"@"+tag2.getName()+"@_@";
            User user123 = User.builder()
                    .email(String.format("user%d@gmail.com",i))
                    .pw("123")
                    .userName(String.format("username%d.com",i))
                    .profile(String.format("userprofile%d.com",i))
                    .hashTagsString(tagString)
                    .tags(userTags)
                    .phoneNumber(String.format("01011112222%d",i))
                    .userStatus(UserStatusEnum.ACTIVE)
                    .field(0).build();
            users.add(userRepository.save(user123));
        }


        for(int i = 1 ; i<=40 ; i++)
        {
            Tap newTap1 = Tap.builder().sendUser(users.get(0)).receiveUser(users.get(i)).msg("gd~").status(0).build();
            newTap1.setInsertDt(LocalDateTime.now());
            sendTaps.add(tapRepository.save(newTap1));
            Thread.sleep(100);
        }
        for(int i = 50 ; i<=83 ; i++)
        {
            Tap newTap1 = Tap.builder().sendUser(users.get(i)).receiveUser(users.get(0)).msg("gd~").status(0).build();
            newTap1.setInsertDt(LocalDateTime.now());
            recieveTaps.add(tapRepository.save(newTap1));
            Thread.sleep(100);
        }
        for(int i = 1 ; i<=9 ; i++)
        {
            String roomId = UUID.randomUUID().toString();
            Friend friend1 = Friend.builder()
                .me(users.get(0))
                .you(users.get(i))
                .roomId(roomId)
                .build();
            Friend friend2 = Friend.builder()
                .me(users.get(i))
                .you(users.get(0))
                .roomId(roomId)
                .build();
            friends.add(friendRepository.save(friend1));
            friends.add(friendRepository.save(friend2));
            roomids.add(roomId);
        }
    }

    @Nested
    @DisplayName("내가 받거나 보낸탭 조회")
    class getMyTaps {
        @Test
        @DisplayName("나한테 탭 보낸 유저 정보조회")
        void myreceivetap_userinfo() {
            User user1 = users.get(0);
            int size = recieveTaps.size() / 12;
            if (recieveTaps.size() % 12 == 0 & size != 0)
                size = size - 1;
            int index = recieveTaps.size() - 1;
            for (int i = 0; i <= size; i++) {
                List<UserTapDto> userTapDtoList1 = userRepository.findMysendORreceiveTapUserInfo(user1.getId(), 1, i);
                for (UserTapDto dto : userTapDtoList1) {
                    assertEquals(recieveTaps.get(index).getId(), dto.getTapId());
                    index = index - 1;
                }
            }
        }

        @Test
        @DisplayName("내가 탭 보낸 유저 정보조회")
        void mysendetap_userinfo() {
            User user1 = users.get(0);
            int size = sendTaps.size() / 12;
            if (sendTaps.size() % 12 == 0 & size != 0)
                size = size - 1;
            int index = sendTaps.size() - 1;
            for (int i = 0; i <= size; i++) {
                List<UserTapDto> userTapDtoList1 = userRepository.findMysendORreceiveTapUserInfo(user1.getId(), 0, i);
                for (UserTapDto dto : userTapDtoList1) {
                    assertEquals(sendTaps.get(index).getId(), dto.getTapId());
                    index = index - 1;
                }
            }
        }
    }

    @Test
    @DisplayName("내친구 정보 조회")
    void myfriends(){
        List<UserTapDto> retDtos = userRepository.findMyFriendsById(users.get(0).getId(),roomids);
        assertEquals(roomids.size(),retDtos.size());
        for(UserTapDto retDto : retDtos)
        {
            assertTrue(roomids.contains(retDto.getRoomId()));
        }
    }

    @Nested
    @DisplayName("검색 기능")
    class search{
        @Test
        @DisplayName("검색 기능 - OR 검색")
        void search_or(){

            int size = 20;
            for(int i = 0;i < 200 ; i++)
            {
                List<String> tagNames = new ArrayList<>();
                int i1 = RandomNumberGeneration.randomRange(1,15);
                int i2 = RandomNumberGeneration.randomRange(16,30);
                HashTag hashtag1 = hashTagRepositoty.findById(hashTags.get(i1).getId()).orElse(null);
                HashTag hashtag2 = hashTagRepositoty.findById(hashTags.get(i2).getId()).orElse(null);
                if(hashtag1 == null | hashtag2 == null)
                    System.out.println("?");
                tagNames.add(hashtag1.getName());
                tagNames.add(hashtag2.getName());
                SearchRequestDto dto = new SearchRequestDto();
                dto.setType(0);
                dto.setPage(0);
                dto.setField(0);
                dto.setSearchTags(tagNames);
                List<UserMainDto> userMainDtos = userRepository.findAllByTag(dto,5L);
                for(UserMainDto mainDto : userMainDtos)
                {
                    boolean check1 = mainDto.getHashTags().contains(hashtag1.getName());
                    boolean check2 = mainDto.getHashTags().contains(hashtag2.getName());
                    assertTrue(check1 | check2);
                    assertFalse(mainDto.getUserId() == 5L);
                }

            }
        }

        @Test
        @DisplayName("검색 기능 - AND 검색")
        void search_and(){

            int size = 20;
            for(int i = 0;i < size ; i++)
            {
                List<String> tagNames = new ArrayList<>();
                int i1 = RandomNumberGeneration.randomRange(1,15);
                int i2 = RandomNumberGeneration.randomRange(16,30);
                HashTag hashtag1 = hashTagRepositoty.findById(hashTags.get(i1).getId()).orElse(null);
                HashTag hashtag2 = hashTagRepositoty.findById(hashTags.get(i2).getId()).orElse(null);
                if(hashtag1 == null | hashtag2 == null)
                    System.out.println("?");
                tagNames.add(hashtag1.getName());
                tagNames.add(hashtag2.getName());
                SearchRequestDto dto = new SearchRequestDto();
                dto.setType(1);
                dto.setPage(0);
                dto.setField(0);
                dto.setSearchTags(tagNames);
                List<UserMainDto> userMainDtos = userRepository.findAllByTag(dto,0L);
                for(UserMainDto mainDto : userMainDtos)
                {
                    boolean check1 = mainDto.getHashTags().contains(hashtag1.getName());
                    boolean check2 = mainDto.getHashTags().contains(hashtag2.getName());
                    assertTrue(check1 & check2);
                }

            }
        }
    }

    @Nested
    @DisplayName("메인페이지 User목록")
    class getRandomUser
    {
        @Test
        @DisplayName("랜덤 유저 조회")
        void getUser() {
            for(int i = 0 ; i < 50 ; i++) {
                List<UserMainDto> mainDtos = userRepository.getRandomUser(userCount, 0L);
                for (UserMainDto dto : mainDtos) {
                    User user = userRepository.findById(dto.getUserId()).orElse(null);
                    assertTrue(cardRepository.findAllByUser(user).size() != 0);
                    assertTrue(user.getTags().size() != 0);
                    assertTrue(user.getUserStatus() == UserStatusEnum.ACTIVE);
                }
            }
        }
        @Test
        @DisplayName("나를 제외한 유저 조회")
        void getUser_excep() {
            for(int i = 0 ; i < 50 ; i++) {
                List<UserMainDto> mainDtos = userRepository.getRandomUser(userCount, 1L);
                for (UserMainDto dto : mainDtos) {
                    User user = userRepository.findById(dto.getUserId()).orElse(null);
                    assertTrue(cardRepository.findAllByUser(user).size() != 0);
                    assertTrue(user.getTags().size() != 0);
                    assertTrue(user.getUserStatus() == UserStatusEnum.ACTIVE);
                    assertTrue(dto.getUserId() != 1L);
                }
            }

        }

    }

    @Test
    @DisplayName("메인화면에 검색될 유저의 수")
    void getActiveUserCount(){
        assertEquals(userCount,userRepository.getActiveUsercnt());
    }

    @Nested
    @DisplayName("UserName 으로 유저의 존재 유무 파악")
    class existUserByUserName{
        @Test
        @DisplayName("True")
        void existUserByUserName_true(){
            for(User user : users)
                assertTrue(userRepository.existUserByUserName(user.getUserName()));
        }
        @Test
        @DisplayName("False")
        void existUserByUserName_false(){
            for(int i = 0 ; i < 20 ; i++)
                assertFalse(userRepository.existUserByUserName(UUID.randomUUID().toString()));
        }

    }

    @Nested
    @DisplayName("PhoneNumber 로 유저의 존재 유무 파악")
    class existUserByPhoneNumber{

        @Test
        @DisplayName("True")
        void existUserByPhoneNumbere_true(){
            for(User user : users)
                assertTrue(userRepository.existUserByPhoneNumber(user.getPhoneNumber()));
        }
        @Test
        @DisplayName("False")
        void existUserByPhoneNumber_false(){
            for(int i = 0 ; i < 20 ; i++)
                assertFalse(userRepository.existUserByPhoneNumber(UUID.randomUUID().toString()));
        }

    }

    @Nested
    @DisplayName("userEmail로 Active유저의 존재 유무 파악")
    class existUserByEmailAndUserStatus{

        @Test
        @DisplayName("True")
        void existUserByEmailAndUserStatus_true(){
            for(int i = 0 ; i < 100 ; i++)
                assertTrue(userRepository.existUserByEmailAndUserStatus(users.get(i).getEmail()));
        }
        @Test
        @DisplayName("INACTIVE 유저")
        void existUserByEmailAndUserStatus_inactive(){
            for(int i = 100 ; i < 110 ; i++)
                assertFalse(userRepository.existUserByEmailAndUserStatus(users.get(i).getEmail()));
        }

        @Test
        @DisplayName("일치하는 Email 없는경우")
        void existUserByEmailAndUserStatus_notfoundemail(){
            for(int i = 0 ; i < 20 ; i++)
                assertFalse(userRepository.existUserByEmailAndUserStatus(UUID.randomUUID().toString()));
        }

    }
}