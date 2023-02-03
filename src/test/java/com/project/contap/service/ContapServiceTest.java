package com.project.contap.service;

import com.project.contap.common.enumlist.DefaultRspEnum;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.chat.ChatRoomRepository;
import com.project.contap.common.Common;
import com.project.contap.common.DefaultRsp;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.friend.SortedFriendsDto;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.model.user.dto.UserTapDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContapServiceTest {

    @Mock
    TapRepository tapRepository;
    @Mock
    ChatRoomRepository chatRoomRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    FriendRepository friendRepository;
    @Mock
    Common common;

    Long tapId = 1L;
    String receiveUserEmail = "lsj2867@gmail.com";
    User imsiSender = User.builder()
            .email("lsj2867@gmail.com")
            .pw("123")
            .userName("lsj2867")
            .field(0).build();
    User imsiReceiver = User.builder()
            .email("lsj4534@gmail.com")
            .pw("123")
            .userName("lsj4534")
            .field(1).build();

    @Nested
    @DisplayName("Tap 거절")
    class rejectTap {
        @Test
        @DisplayName("정상처리")
        void tapReject_good() {
            Tap mockTap = Tap.builder().id(tapId).sendUser(imsiSender).receiveUser(imsiReceiver).msg("gd").status(0).build();
            ContapService cantapService = new ContapService(tapRepository, chatRoomRepository, userRepository, friendRepository, common);
            when(tapRepository.findById(tapId))
                    .thenReturn(Optional.of(mockTap));
            DefaultRsp dfRsp = cantapService.tapReject(tapId, imsiReceiver.getEmail());
            assertEquals("정상적으로 처리 되었습니다.", dfRsp.getMsg());

        }

        @Test
        @DisplayName("해당탭을 못찾은경우")
        void tapReject_notfound() {
            Tap mockTap = null;
            ContapService cantapService = new ContapService(tapRepository, chatRoomRepository, userRepository, friendRepository, common);
            when(tapRepository.findById(tapId))
                    .thenReturn(Optional.ofNullable(mockTap));
            DefaultRsp dfRsp = cantapService.tapReject(tapId, imsiReceiver.getEmail());
            assertEquals("해당 tab이 존재하지 않습니다 TabID를 다시확인해주세요..", dfRsp.getMsg());
        }
    }

    @Nested
    @DisplayName("Tap 수락")
    class acceptTap {
        @Test
        @DisplayName("정상처리")
        void tapAccept_good() {
            Tap mockTap = Tap.builder().id(tapId).sendUser(imsiSender).receiveUser(imsiReceiver).msg("gd").status(0).build();
            ContapService cantapService = new ContapService(tapRepository, chatRoomRepository, userRepository, friendRepository, common);
            when(tapRepository.findById(tapId))
                    .thenReturn(Optional.of(mockTap));
            DefaultRsp dfRsp = cantapService.rapAccept(tapId, imsiReceiver.getEmail());
            assertEquals("정상적으로 처리 되었습니다.", dfRsp.getMsg());
        }

        @Test
        @DisplayName("해당탭을 못찾은경우")
        void tapAccept_notfound() {
            Tap mockTap = null;
            ContapService cantapService = new ContapService(tapRepository, chatRoomRepository, userRepository, friendRepository, common);
            when(tapRepository.findById(tapId))
                    .thenReturn(Optional.ofNullable(mockTap));
            DefaultRsp dfRsp = cantapService.rapAccept(tapId, imsiReceiver.getEmail());
            assertEquals("해당 tab이 존재하지 않습니다 TabID를 다시확인해주세요..", dfRsp.getMsg());
        }
    }

    @Nested
    @DisplayName("그랩 목록 조회")
    class getMyFriends {
        @Test
        @DisplayName("그랩 목록 조회")
        void getMyFriends_good() {
            List<String> room = new ArrayList<>(); // roomid들
            List<String> roomStatus = new ArrayList<>(); // roomstatus들
            List<String> date = new ArrayList<>(); // date들
            List<String> resultdate = new ArrayList<>(); // date들
            room.add("a");
            room.add("b");
            room.add("c");//"@@/0/_test용_"
            roomStatus.add("@@/0/_test용_");
            roomStatus.add("@@/0/_test용_");
            roomStatus.add("@@/0/_test용_");
            date.add("2.11125143243E11");
            date.add("2.11125133243E11");
            date.add("2.11125123243E11");
            resultdate.add("211125143243");
            resultdate.add("211125133243");
            resultdate.add("211125123243");
            UserTapDto dto1 = new UserTapDto(2L,"dto1_email","dto1_profile","dto1_userName","dto1_hashTags",0,"b",0,1L);
            UserTapDto dto2 = new UserTapDto(3L,"dto2_email","dto2_profile","dto2_userName","dto2_hashTags",0,"a",0,2L);
            UserTapDto dto3 = new UserTapDto(4L,"dto3_email","dto3_profile","dto3_userName","dto3_hashTags",0,"c",0,3L);
            List<Long> sortedUserId = new ArrayList<>();
            sortedUserId.add(3L);
            sortedUserId.add(2L);
            sortedUserId.add(4L);
            List<List<String>> order = new ArrayList<>();
            order.add(room);
            order.add(roomStatus);
            order.add(date);
            int type = 0;
            int page = 0;
            List<UserTapDto> myFriendsUserDto = new ArrayList<>();
            myFriendsUserDto.add(dto1);
            myFriendsUserDto.add(dto2);
            myFriendsUserDto.add(dto3);
            User mockUser = User.builder().id(1L).email("123@123").build();
            ContapService cantapService = new ContapService(tapRepository, chatRoomRepository, userRepository, friendRepository, common);
            when(chatRoomRepository.getMyFriendsOrderByDate(page,mockUser.getEmail(),type))
                    .thenReturn(order);
            when(userRepository.findMyFriendsById(mockUser.getId(),room))
                    .thenReturn(myFriendsUserDto);

            List<SortedFriendsDto> ret = cantapService.getMyfriends(mockUser, type,page);
            for (int i = 0 ; i < ret.size() ; i++)
            {
                assertEquals(room.get(i),ret.get(i).getRoomId());
                assertEquals(roomStatus.get(i),ret.get(i).getRoomStatus());
                assertEquals(resultdate.get(i),ret.get(i).getDate());
                assertEquals(sortedUserId.get(i),ret.get(i).getUserId());
            }


        }
    }

    @Test
    @DisplayName("내가 보낸 탭 조회")
    void getMyDoTap() {
        User user = User.builder().id(1L).build();
        int page = 0;
        ContapService cantapService = new ContapService(tapRepository, chatRoomRepository, userRepository, friendRepository, common);
        List<UserTapDto> ret = cantapService.getMydoTap(user,page);
        verify(userRepository,times(1)).findMysendORreceiveTapUserInfo(eq(user.getId()),eq(0),eq(page));
    }

    @Test
    @DisplayName("내가 받은 탭 조회")
    void getMyTap() {
        UserTapDto tapDto1 = new UserTapDto(1L,"email","profile","username","hash",1L,"gd",1,1);
        UserTapDto tapDto2 = new UserTapDto(2L,"email","profile","username","hash",2L,"gd",1,0);
        List<UserTapDto> dtos = new ArrayList<>();
        Tap tap = Tap.builder().build();
        dtos.add(tapDto1);
        dtos.add(tapDto2);
        User user = User.builder().id(1L).build();
        int page = 0;
        when(userRepository.findMysendORreceiveTapUserInfo(eq(user.getId()),eq(1),eq(page)))
                .thenReturn(dtos);
        when(tapRepository.findById(any(Long.class))).thenReturn(Optional.of(tap));
        ContapService cantapService = new ContapService(tapRepository, chatRoomRepository, userRepository, friendRepository, common);
        cantapService.getMyTap(user,page);
        verify(tapRepository,times(1)).saveAll(anyList());
    }

    @Nested
    @DisplayName("친구관계 삭제")
    class deleteFriend
    {
        @Test
        @DisplayName("정상")
        void OK() {
            User user1 = User.builder().email("email1").id(1L).build();
            User user2 = User.builder().email("email2").id(2L).build();
            Friend friend1 = Friend.builder().roomId("roomId").build();
            Friend friend2 = Friend.builder().roomId("roomId").build();
            Long userId = 2L;
            when(userRepository.findById(eq(userId))).thenReturn(Optional.of(user2));
            when(friendRepository.getFriend(eq(user1),eq(user2))).thenReturn(friend1);
            when(friendRepository.getFriend(eq(user2),eq(user1))).thenReturn(friend2);
            ContapService cantapService = new ContapService(tapRepository, chatRoomRepository, userRepository, friendRepository, common);
            cantapService.delFriend(user1,userId);
            verify(chatRoomRepository,times(1)).whendeleteFriend(friend1.getRoomId(),user1.getEmail(),user2.getEmail());
            verify(friendRepository,times(1)).delete(friend1);
            verify(friendRepository,times(1)).delete(friend2);
        }
        @Test
        @DisplayName("비정상")
        void Error() {
            User user1 = User.builder().email("email1").id(1L).build();
            Long userId = 2L;
            ContapService cantapService = new ContapService(tapRepository, chatRoomRepository, userRepository, friendRepository, common);
            ContapException exception = assertThrows(ContapException.class, () -> {
                cantapService.delFriend(user1,userId);
            });
            assertEquals(exception.getErrorCode(), ErrorCode.USER_NOT_FOUND);
        }
    }
    @Nested
    @DisplayName("보낸 탭 취소")
    class cancleTap
    {
        @Test
        @DisplayName("정상")
        void OK() {
            User user = User.builder().email("email1").id(1L).build();
            Long tagId = 2L;
            ContapService cantapService = new ContapService(tapRepository, chatRoomRepository, userRepository, friendRepository, common);
            Tap tap = Tap.builder().sendUser(user).build();
            when(tapRepository.findById(eq(tagId))).thenReturn(Optional.of(tap));
            DefaultRsp ret = cantapService.deleteTap(tagId,user);
            assertEquals(ret.getMsg() , DefaultRspEnum.OK.getValue());
        }
        @Test
        @DisplayName("비정상 - NotFound")
        void Error_NotFound() {
            User user = User.builder().email("email1").id(1L).build();
            Long tagId = 2L;
            ContapService cantapService = new ContapService(tapRepository, chatRoomRepository, userRepository, friendRepository, common);
            DefaultRsp ret = cantapService.deleteTap(tagId,user);
            assertEquals(ret.getMsg() , DefaultRspEnum.NOT_FOUND_TAP.getValue());
        }
        @Test
        @DisplayName("비정상 - Wrong")
        void Error_Wrong() {
            User user1 = User.builder().email("email1").id(1L).build();
            User user2 = User.builder().email("email2").id(2L).build();
            Long tagId = 2L;
            ContapService cantapService = new ContapService(tapRepository, chatRoomRepository, userRepository, friendRepository, common);
            Tap tap = Tap.builder().sendUser(user2).build();
            when(tapRepository.findById(eq(tagId))).thenReturn(Optional.of(tap));
            DefaultRsp ret = cantapService.deleteTap(tagId,user1);
            assertEquals(ret.getMsg() , DefaultRspEnum.WRONG.getValue());
        }

    }

}