package com.project.contap.model.user;

import com.project.contap.common.SearchRequestDto;
import com.project.contap.model.user.dto.UserMainDto;
import com.project.contap.model.user.dto.UserTapDto;

import java.util.List;

public interface CustomUserRepository {
    List<UserTapDto> findMysendORreceiveTapUserInfo(Long userId, int type, int page);
    List<UserTapDto> findMyFriendsById(Long userId,List<String> orderList);
    List<UserMainDto> findAllByTag(SearchRequestDto tagsandtype,Long userId);
    List<UserMainDto> getRandomUser(Long usercnt,Long myId);
    Boolean existUserByUserName(String userName);
    Boolean existUserByPhoneNumber(String phoneNumber);
    Long getActiveUsercnt();
    Boolean existUserByEmailAndUserStatus(String email);
}
