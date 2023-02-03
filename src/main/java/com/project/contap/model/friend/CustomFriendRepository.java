package com.project.contap.model.friend;

import com.project.contap.model.user.User;

import java.util.List;

public interface CustomFriendRepository {
    Boolean checkFriend(User me, User you);
    Friend getFriend(User me, User you);
    List<Friend> getallmyFriend(User user);
}
