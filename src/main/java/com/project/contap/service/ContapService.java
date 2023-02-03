package com.project.contap.service;

import com.project.contap.model.chat.ChatRoomRepository;
import com.project.contap.common.Common;
import com.project.contap.common.DefaultRsp;
import com.project.contap.common.enumlist.DefaultRspEnum;
import com.project.contap.common.enumlist.MsgTypeEnum;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.friend.SortedFriendsDto;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.model.user.dto.UserTapDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ContapService {

    private final TapRepository tapRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final Common common;

    public List<UserTapDto> getMydoTap(User user,int page) {
        List<UserTapDto> mySendTapUserDto = userRepository.findMysendORreceiveTapUserInfo(user.getId(),0,page);
        return mySendTapUserDto;
    }

    @Transactional
    public List<UserTapDto> getMyTap(User user,int page) {
        List<UserTapDto> myReceiveTapUserDto = userRepository.findMysendORreceiveTapUserInfo(user.getId(),1,page);
        setNewFriendFalse(myReceiveTapUserDto);
        return myReceiveTapUserDto;
    }



    public DefaultRsp tapReject(Long tagId, String receiveUserEmail) {
        Tap tap = tapRepository.findById(tagId).orElse(null);
        if (tap != null)
        {
            common.sendAlarmIfneeded(MsgTypeEnum.REJECT_TAP,receiveUserEmail,tap.getSendUser().getEmail(),tap.getReceiveUser());
            tapRepository.delete(tap);
            return new DefaultRsp(DefaultRspEnum.OK);
        }
        return new DefaultRsp(DefaultRspEnum.NOT_FOUND_TAP);
    }

    @Transactional
    public DefaultRsp rapAccept(Long tagId,String receiveUserEmail) {
        Tap tap = tapRepository.findById(tagId).orElse(null);
        if (tap != null)
        {
            User sendUser = tap.getSendUser();
            common.makeChatRoom(tap.getSendUser(),tap.getReceiveUser());
            common.sendAlarmIfneeded(MsgTypeEnum.ACCEPT_TAP,receiveUserEmail,sendUser.getEmail(), tap.getReceiveUser());
            tapRepository.delete(tap);
            return new DefaultRsp(DefaultRspEnum.OK);
        }
        return new DefaultRsp(DefaultRspEnum.NOT_FOUND_TAP);
    }

    @Transactional
    public List<SortedFriendsDto>getMyfriends(User user,int type,int page) {
        // 0 이면 페이징처리
        List<List<String>> order = chatRoomRepository.getMyFriendsOrderByDate(page,user.getEmail(),type);
        List<UserTapDto> myFriendsUserDto = new ArrayList<>();
        List<SortedFriendsDto> ret = new ArrayList<>();

        if(order.get(0).size() != 0) {
            myFriendsUserDto = userRepository.findMyFriendsById(user.getId(),order.get(0));
            ret = sortFriendList(order,myFriendsUserDto,type==0);
        } // 페이지 기능이 사라지는게 확정이된다면 다른방법을 고려해보도록하자..

        return ret;
    }

    private List<SortedFriendsDto> sortFriendList
            (List<List<String>> order,
             List<UserTapDto> myFriendsUserDto,
             boolean newFriend) {
        SortedFriendsDto dtoArrays[] = new SortedFriendsDto[order.get(0).size()];
        Map<String, Integer> sortInfo = new HashMap<>();
        Boolean doSave = false;
        List<Long> friendIds = new ArrayList<>();
        for(int i = 0 ; i < order.get(0).size();i++)
        {
            sortInfo.put(order.get(0).get(i),i);
            dtoArrays[i] = new SortedFriendsDto();
            dtoArrays[i].setRoomStatus(order.get(1).get(i));
            for(int j = 0 ; j < 3 ; j++) {
                if (dtoArrays[i].getRoomStatus() == null)
                    dtoArrays[i].setRoomStatus(chatRoomRepository.getRoomStatus(order.get(0).get(i)));
                else
                    break;
            }

            dtoArrays[i].setRoomId(order.get(0).get(i));
            String date = order.get(2).get(i);
            int e = date.indexOf("E");
            date = date.substring(0,e).replace(".","");
            dtoArrays[i].setDate(date);
        }
        for(UserTapDto userDto : myFriendsUserDto)
        {
            dtoArrays[sortInfo.get(userDto.getRoomId())].setFromUserDto(userDto,chatRoomRepository.getSessionId(userDto.getEmail()) != null);
            if (newFriend  && userDto.getNewFriend())
                friendIds.add(userDto.getFriendId());
        }
        if (newFriend && friendIds.size()>0) {
            List<Friend> friends;
            friends = friendRepository.findAllById(friendIds);
            for(Friend friend : friends)
                friend.setNewFriend(0);
            friendRepository.saveAll(friends);
        }
        return Arrays.asList(dtoArrays);
    }

    @Transactional
    public DefaultRsp delFriend(User user, Long userId) {
        User secUser = userRepository.findById(userId).orElse(null);
        if (secUser==null)
            throw new ContapException(ErrorCode.USER_NOT_FOUND);
        Friend fir = friendRepository.getFriend(user,secUser);
        Friend sec = friendRepository.getFriend(secUser,user);
        chatRoomRepository.whendeleteFriend(fir.getRoomId(),user.getEmail(),secUser.getEmail());
        friendRepository.delete(fir);
        friendRepository.delete(sec);
        return new DefaultRsp(DefaultRspEnum.OK);
    }

    private void setNewFriendFalse(List<UserTapDto> myReceiveTapUserDto) {
        List<Tap> taps = new ArrayList<>();
        for(UserTapDto userTapDto : myReceiveTapUserDto) {
            if(userTapDto.getNewFriend())
                taps.add(tapRepository.findById(userTapDto.getTapId()).orElse(null));
        }
        for(Tap tap : taps)
        {
            tap.setNewFriend(0);
        }
        if(taps.size()>0)
            tapRepository.saveAll(taps);
    }
    @Transactional
    public DefaultRsp deleteTap(Long tagId, User user) {
        Tap tap = tapRepository.findById(tagId).orElse(null);
        if(tap == null)
            return new DefaultRsp(DefaultRspEnum.NOT_FOUND_TAP);
        if(!tap.getSendUser().getId().equals(user.getId()))
            return new DefaultRsp(DefaultRspEnum.WRONG);
        tapRepository.delete(tap);
        return new DefaultRsp(DefaultRspEnum.OK);
    }
}