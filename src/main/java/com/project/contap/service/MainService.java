package com.project.contap.service;

import com.project.contap.common.Common;
import com.project.contap.common.DefaultRsp;
import com.project.contap.common.SearchRequestDto;
import com.project.contap.common.enumlist.AuthorityEnum;
import com.project.contap.common.enumlist.DefaultRspEnum;
import com.project.contap.common.enumlist.MsgTypeEnum;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.card.dto.QCardDto;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.hashtag.HashTag;
import com.project.contap.model.hashtag.HashTagRepositoty;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.model.user.dto.UserMainDto;
import com.project.contap.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {
    private final HashTagRepositoty hashTagRepositoty;
    private final UserRepository userRepository;
    private final TapRepository tapRepository;
    private final FriendRepository friendRepository;
    private final CardRepository cardRepository;
    private final UserService userService;
    private final Common common;

    public List<HashTag> getHashTag() {
        return hashTagRepositoty.findAll();
    }

    public List<UserMainDto> searchuser(SearchRequestDto tagsandtype,Long userId) {
        List<UserMainDto> ret = userRepository.findAllByTag(tagsandtype,userId);
        return ret;
    }

    public List<QCardDto> getCards(Long userId) {
        List<QCardDto> ret =  cardRepository.findAllByUserId(userId);
        Collections.reverse(ret);
        return ret;
    }

    public List<UserMainDto> getUserDtoList(UserDetailsImpl userDetails) {
        Long myId = 0L;
        if(userDetails != null)
            myId = userDetails.getUser().getId();
        List<UserMainDto> ret = userRepository.getRandomUser(User.userCount,myId);
        return ret;
    }


    @Transactional
    public DefaultRsp dotap(User sendUser, Long otherUserId,String msg) {
        if(sendUser.getId().equals(otherUserId))
            return new DefaultRsp(DefaultRspEnum.CANT_SEND_TO_ME);
        User receiveUser = userRepository.findById(otherUserId).orElse(null);
        Boolean checkFriend = friendRepository.checkFriend(sendUser,receiveUser);
        if (checkFriend)
            return new DefaultRsp(DefaultRspEnum.ALREADY_FRIEND);

        Tap checkrecievetap = tapRepository.checkReceiveTap(receiveUser,sendUser);
        if (checkrecievetap != null)
        {
            common.makeChatRoom(sendUser,receiveUser);
            tapRepository.delete(checkrecievetap);
            return new DefaultRsp(DefaultRspEnum.ACCEPT_TAP);
        }

        Boolean checksendtap = tapRepository.checkSendTap(receiveUser,sendUser);
        if (checksendtap)
            return new DefaultRsp(DefaultRspEnum.ALREADY_SEND);

        Tap newTap = Tap.builder()
                .sendUser(sendUser)
                .receiveUser(receiveUser)
                .msg(msg)
                .newFriend(1)
                .build();
        tapRepository.save(newTap);
        common.sendAlarmIfneeded(MsgTypeEnum.SEND_TAP,sendUser.getEmail(),receiveUser.getEmail(), receiveUser);
        return new DefaultRsp(DefaultRspEnum.OK);
    }

    public void tutorial(int tutorialNum, User user) {
        int authStatus = user.getAuthStatus();
        if(tutorialNum == 0) { //phone
            authStatus = authStatus| AuthorityEnum.PHONE_TUTORIAL.getAuthority();
        }
        else if(tutorialNum == 1) { // profile
            authStatus = authStatus|AuthorityEnum.PROFILE_TUTORIAL.getAuthority();
        }
        user.setAuthStatus(authStatus);
        userRepository.save(user);
    }

    public int getUserAuthStatus(User user) {
        return user.getAuthStatus();
    }
}